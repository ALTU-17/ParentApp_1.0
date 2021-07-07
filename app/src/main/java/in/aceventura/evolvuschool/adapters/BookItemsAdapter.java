package in.aceventura.evolvuschool.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import in.aceventura.evolvuschool.DataSet;
import in.aceventura.evolvuschool.R;

/**
 * Created by Vinod Patil on 30-08-2017.
 */

public class BookItemsAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<DataSet> DataList;


    public BookItemsAdapter(Activity activity, List<DataSet> remarkItems) {
        this.activity = activity;
        this.DataList = remarkItems;
    }

    @Override
    public int getCount() {
        return DataList.size();
    }

    @Override
    public Object getItem(int location) {
        return DataList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.book_row, null);


        TextView bookTitle = convertView.findViewById(R.id.book_title);
        TextView bookAuthor = convertView.findViewById(R.id.book_author);
        TextView bookCategory = convertView.findViewById(R.id.book_category);
        DataSet m = DataList.get(position);

        bookTitle.setText(m.getbTitle());
        bookAuthor.setText(m.getbAuthor());
        bookCategory.setText(m.getbCategoryName());

        return convertView;
    }

}

