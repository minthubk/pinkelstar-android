package com.pinkelstar.android.ui.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.pinkelstar.android.server.Constants;
import com.pinkelstar.android.server.Server;
import com.pinkelstar.android.server.Utils;
import com.pinkelstar.android.ui.ImageCache;

public class ImagePreloaderTask extends AsyncTask<Void, Void, Void> {
	private ImageCache imageCache;
	
	public static void initialize(ImageCache imageCache) {
		new ImagePreloaderTask(imageCache);
	}
	
	public ImagePreloaderTask(ImageCache imageCache) {
		this.imageCache = imageCache;
		this.execute();
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		Log.d("PinkelStar", "preloading images");
		switch (Server.getInstance().getState()) {
		case Server.STATE_INITIALIZED:
			preloadIcons();
			Log.d("PinkelStar", "images preloaded");
			break;
		case Server.STATE_ERROR:
			Log.d("PinkelStar", "preloading of images cancelled due to an issue initializing the server");
			break;
		}
		return null;
	}
	
	private void preloadIcons() {
		imageCache.preloadDrawable(Server.getInstance().getIconUrl());
		for (String networkName : Server.getInstance().getKnownNetworks()) {
			String networkUrl = Utils.buildImageUrl(networkName, Constants.LARGE_IMAGES);
			imageCache.preloadDrawable(networkUrl);
		}
	}
}