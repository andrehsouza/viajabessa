package br.com.viajabessa.util;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class ViajabessaImages extends Application {

	private static ImageLoader imageLoader = ImageLoader.getInstance();

	public static void iniciaImageLoader(Context c) {
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
				c).threadPriority(Thread.MAX_PRIORITY)
				.denyCacheImageMultipleSizesInMemory()
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.memoryCache(new WeakMemoryCache()).build();
		imageLoader.init(configuration);
	}

	public static boolean isIniciado() {
		return imageLoader.isInited();
	}

	public static void fechaImageLoader() {
		imageLoader.stop();
	}

	public static ImageLoader getImageLoader() {
		return imageLoader;
	}
}
