package com.example.panjunchen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.panjunchen.models.News;
import com.example.panjunchen.models.TableConfig;
import com.example.panjunchen.models.TableOperate;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class SingleItemClickListener extends RecyclerView.SimpleOnItemTouchListener   {
    private OnItemClickListener   clickListener;
    private GestureDetectorCompat   gestureDetector;



    public interface OnItemClickListener   {
        void onItemClick(View   view, int position);
        void onItemLongClick(View   view, int position);
    }



    public SingleItemClickListener(final RecyclerView   recyclerView,
                             OnItemClickListener   listener) {
        this.clickListener   = listener;
        gestureDetector   = new GestureDetectorCompat(recyclerView.getContext(),
                new   GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public   boolean onSingleTapUp(MotionEvent e) {
                        View   childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                        if   (childView != null && clickListener != null) {
                            clickListener.onItemClick(childView,   recyclerView.getChildAdapterPosition(childView));
                        }
                        return   true;
                    }



                    @Override

                    public   void onLongPress(MotionEvent e) {

                        View   childView = recyclerView.findChildViewUnder(e.getX(), e.getY());

                        if   (childView != null && clickListener != null) {

                            clickListener.onItemLongClick(childView,

                                    recyclerView.getChildAdapterPosition(childView));

                        }

                    }

                });

    }



    @Override

    public boolean onInterceptTouchEvent(RecyclerView   rv, MotionEvent e) {

        gestureDetector.onTouchEvent(e);

        return false;

    }

}

public class Section extends Fragment {
    private String secname;
    private NewsAdapter adapter;
    private RecyclerView rv;
    private RefreshLayout refresh;
    private List<News> list = new ArrayList<News>();
    private TableOperate db;
    private int index;
    public Section(){
        super();
    }

    public Section(String name)
    {
        super();
        secname=name;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("DEBUG:","onCreateView:"+secname);
        if(adapter!=null)
        {
            rv.setAdapter(adapter);
        }
        View view=inflater.inflate(R.layout.section,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Log.d("DEBUG:","onActivityCreated:"+secname);
        init();
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                Log.d("DEBUG:","refresh:"+secname);
                List<News> k=TableOperate.getInstance().getNewsFromServer(secname,10);
                for(int i=0;i<k.size();i++)
                {
                    list.add(0 ,k.get(i));
                }
                adapter.notifyDataSetChanged();
                index=10;
                refreshlayout.finishRefresh();
            }
        });
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {

                List<News> k = TableOperate.getInstance().getNewsFromLocal(secname, 10, index);
                if(k.size()==0)
                {
                    refreshlayout.finishLoadMoreWithNoMoreData();
                }
                else
                {
                    index = index + k.size();
                    for (int i = 0; i < k.size(); i++) {
                        list.add(k.get(i));
                    }
                    adapter.notifyDataSetChanged();
                    refreshlayout.finishLoadMore();
                }
            }
        });

    }

    private void init() {
        rv=getView().findViewById(R.id.newslist);
        db=TableOperate.getInstance();
        refresh=getView().findViewById(R.id.refreshLayout);
        list=db.getNewsFromLocal(secname,10,0);
        index=list.size();
        adapter=new NewsAdapter(list,getContext());
        rv.setAdapter(adapter);
        rv.addOnItemTouchListener(new SingleItemClickListener(rv, new SingleItemClickListener.OnItemClickListener(){

            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(getContext(),ReadActivity.class);
                list.get(position).setReadtime(new Date());
                TableOperate.getInstance().renewNews(list.get(position));
                adapter.notifyDataSetChanged();
                intent.putExtra("index",list.get(position).getDBindex());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Log.d("DEBUG:","long click:"+position);
            }
        }));
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
    }


}
