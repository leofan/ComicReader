package com.fll.comicreader.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.fll.comicreader.comicreader.R;
import com.fll.comicreader.dao.Section;

import java.util.List;

/**
 * Created by Administrator on 2016/6/25.
 */
public class SectionGridAdaptor extends BaseAdapter {

    LayoutInflater mInflater;
    private List<Section> sectionList;

    public SectionGridAdaptor(Context context, List<Section> sectionList) {
        this.mInflater = LayoutInflater.from(context);
        this.sectionList = sectionList;
    }

    @Override
    public int getCount() {
        return sectionList.size();
    }

    @Override
    public Object getItem(int position) {
        return sectionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.section_item, null);
            viewHolder.button = (Button) convertView.findViewById(R.id.section_button);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.button.setText(sectionList.get(position).getName());
        return convertView;
    }

    class ViewHolder {
        Button button;
    }
}
