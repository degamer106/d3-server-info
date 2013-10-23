package com.degamer106.serverinfo.ui;

import com.degamer106.serverinfo.R;
import com.degamer106.serverinfo.adapter.ServerCursorAdapter;
import com.degamer106.serverinfo.model.ServerStatusProvider;

import android.app.Activity;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BoxFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
	private static final String KEY_POSITION = "position";
	private static final int SERVER_STATUS_ID = 0;
	private ServerCursorAdapter mAdapter;
	
	private final ContentObserver mObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            if (getActivity() == null) {
                return;
            }

            Loader<Cursor> loader = getLoaderManager().getLoader(SERVER_STATUS_ID);
            if (loader != null) {
                loader.forceLoad();
            }
        }
    };
	
	public static BoxFragment newInstance(int position) {
		BoxFragment fragment = new BoxFragment();		
		Bundle bundle = new Bundle();
		
		bundle.putInt(KEY_POSITION, position);
		fragment.setArguments(bundle);
		
		return fragment;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().initLoader(0, getArguments(), this);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		activity.getContentResolver().registerContentObserver(ServerStatusProvider.CONTENT_URI, true, mObserver);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mAdapter = new ServerCursorAdapter(getActivity(), null);
		setListAdapter(mAdapter);
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		getActivity().getContentResolver().unregisterContentObserver(mObserver);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.listview_fragment, container, false);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {		
		switch (id) {
			case SERVER_STATUS_ID:
				Uri baseUri = ServerStatusProvider.CONTENT_URI;
				String [] projection = null;
				String selection = "position=?";
				String [] selectionArgs = new String [] { Integer.toString(args.getInt(KEY_POSITION)) };
				String sortOrder = null;
				
				return new CursorLoader(getActivity(), baseUri, projection, selection, selectionArgs, sortOrder);
		}
		
		return null;		
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		mAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {}
}
