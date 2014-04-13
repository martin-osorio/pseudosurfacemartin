package com.doghouse.physicssimluator.views;

import com.doghouse.physicssimluator.R;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.OpenGraphAction;
import com.facebook.widget.FacebookDialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class ShareDialogFragment extends DialogFragment implements View.OnClickListener{
	
	public static final String SHARE_MESSAGE = "Share Message";
	
	private UiLifecycleHelper uiHelper;
	
	public static ShareDialogFragment getInstance(){
		ShareDialogFragment fragment = new ShareDialogFragment();
		Bundle args = new Bundle();
		args.putString("message", SHARE_MESSAGE);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    uiHelper = new UiLifecycleHelper(this.getActivity(), null);
	    uiHelper.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		return inflater.inflate(R.layout.share_dialog, null);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		  Dialog dialog = new Dialog(this.getActivity(),android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

		  // request a window without the title
		  dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		  return dialog;
	}
	
	@Override
	public void onActivityCreated(Bundle bundle) {
		super.onActivityCreated(bundle);
		getView().findViewById(R.id.share_facebook_button).setOnClickListener(this);
		getView().findViewById(R.id.share_twitter_button).setOnClickListener(this);
		getView().findViewById(R.id.share_other_button).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.share_facebook_button:
				FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this.getActivity())
				.setLink("android google play link").setDescription(SHARE_MESSAGE).setApplicationName("Android Phsyics Simulator")
		        .build();
				shareDialog.present();
//				uiHelper.trackPendingDialogCall(shareDialog.present());
				
				break;
			case R.id.share_twitter_button:
				String tweetUrl = "https://twitter.com/intent/tweet?text="+SHARE_MESSAGE;
				Uri uri = Uri.parse(tweetUrl);
				startActivity(new Intent(Intent.ACTION_VIEW, uri));
				break;
			case R.id.share_other_button:
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, SHARE_MESSAGE);
				sendIntent.setType("text/plain");
				startActivity(sendIntent);
				break;
		}
		// close dialog
		this.dismiss();
	}
}
