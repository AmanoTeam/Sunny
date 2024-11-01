package com.amanoteam.sunny;

import android.content.Context;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

public class Sunny implements IXposedHookLoadPackage {

	private static final String[] PACKAGES = {
		"eu.kanade.tachiyomi",
		"eu.kanade.tachiyomi.debug",
		"xyz.jmir.tachiyomi.mi",
		"xyz.jmir.tachiyomi.mi.debug",
		"tachiyomi.mangadex",
		"tachiyomi.mangadex.debug",
		"eu.kanade.tachiyomi.j2k",
		"eu.kanade.tachiyomi.j2k.debug",
		"eu.kanade.tachiyomi.sy",
		"eu.kanade.tachiyomi.sy.debug",
		"eu.kanade.tachiyomi.az",
		"eu.kanade.tachiyomi.az.debug",
		"app.mihon",
		"app.mihon.debug",
		"eu.kanade.tachiyomi.yokai",
		"eu.kanade.tachiyomi.yokai.debug",
		"eu.kanade.tachiyomi.nightlyYokai",
		"xyz.luft.tachiyomi.mi",
		"xyz.luft.tachiyomi.mi.debug",
		"com.dark.animetailv2",
		"com.dark.animetailv2.debug",
		"app.komikku",
		"app.komikku.beta"
	};

	private static final String rateLimitInterceptor = "RateLimitInterceptor";

	private static final XC_MethodHook rateLimitHook = new XC_MethodHook() {

		@Override
		protected void beforeHookedMethod(final MethodHookParam methodHookParam) throws Throwable {
			final Object thisObject = methodHookParam.thisObject;
			final Object argument = methodHookParam.args[0];
			final String className = argument.getClass().getSimpleName();

			if (className.equals(rateLimitInterceptor)) {
				methodHookParam.setResult(thisObject);
			}
		}

	};

	private static final XC_MethodHook concurrentDownloadsHook = new XC_MethodHook() {

		@Override
		protected void beforeHookedMethod(final MethodHookParam methodHookParam) throws Throwable {
			final int maxConcurrency = (int) methodHookParam.args[1];
			methodHookParam.args[1] = maxConcurrency * 8;
		}

	};

	public void handleLoadPackage(final LoadPackageParam loadPackageParam) throws Throwable {

		boolean matches = false;

		for (String name : PACKAGES) {
			matches = loadPackageParam.packageName.equals(name);

			if (matches) {
				break;
			}
		}

		if (!matches) {
			return;
		}

		findAndHookMethod("android.app.Application", loadPackageParam.classLoader, "attach", Context.class, new XC_MethodHook() {

				@Override
				protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
					final Context context = (Context) param.args[0];
					final ClassLoader classLoader = context.getClassLoader();

					final Class interceptorClass = findClass("okhttp3.Interceptor", loadPackageParam.classLoader);
					findAndHookMethod("okhttp3.OkHttpClient$Builder", classLoader, "addInterceptor", interceptorClass, rateLimitHook);

					final Class function1Class = findClass("rx.functions.Func1", loadPackageParam.classLoader);

					findAndHookMethod("rx.Observable", classLoader, "flatMap", function1Class, int.class, concurrentDownloadsHook);

					final Class flowClass = findClass("kotlinx.coroutines.flow.Flow", loadPackageParam.classLoader);
					final Class function2Class = findClass("kotlin.jvm.functions.Function2", loadPackageParam.classLoader);

					findAndHookMethod("kotlinx.coroutines.flow.FlowKt", classLoader, "flatMapMerge", flowClass, int.class, function2Class, concurrentDownloadsHook);
				}

			}

		);

	}

}
