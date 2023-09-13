package com.amanoteam.sunny;

import android.content.Context;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

public class Sunny implements IXposedHookLoadPackage {
	
	private static final String PACKAGES[] = {
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
		"eu.kanade.tachiyomi.az.debug"
	};
	
	private static final String rateLimitInterceptor = "eu.kanade.tachiyomi.network.interceptor.RateLimitInterceptor";
	
	private static final XC_MethodHook methodHook = new XC_MethodHook() {
		
		@Override
		protected void beforeHookedMethod(final MethodHookParam methodHookParam) throws Throwable {
			final Object thisObject = methodHookParam.thisObject;
			methodHookParam.setResult(thisObject);
			
			final Object argument = methodHookParam.args[0];
			final String className = argument.getClass().getName();
			
			if (className.equals(rateLimitInterceptor)) {
				methodHookParam.setResult(thisObject);
			}
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
					
					final Class clazz = findClass("okhttp3.Interceptor", loadPackageParam.classLoader);
					findAndHookMethod("okhttp3.OkHttpClient$Builder", classLoader, "addInterceptor", clazz,  methodHook);
				}
				
			}
			
		);
		
	}
	
}
