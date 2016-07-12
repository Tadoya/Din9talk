package io.github.tadoya.din9talk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.tadoya.din9talk.R;

/**
 * Created by choiseongsik on 2016. 7. 5..
 */

public class ListFragment extends Fragment{
    private static final String TAG = "ListFragment";

    protected RecyclerView mRecycler;

    public ListFragment() {}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_all_list, container, false);

        mRecycler = (RecyclerView) rootView.findViewById(R.id.recycle_list);
        mRecycler.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
