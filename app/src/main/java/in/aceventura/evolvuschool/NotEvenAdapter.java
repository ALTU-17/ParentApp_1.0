package in.aceventura.evolvuschool;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class NotEvenAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<DataSet> DataList;

    public NotEvenAdapter(Activity activity, List<DataSet> attachmentItems) {
        this.activity = activity;
        this.DataList = attachmentItems;
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
            //Toast.makeText(activity, "hii", Toast.LENGTH_SHORT).show();

            convertView = inflater.inflate(R.layout.notice_attachment_data, null);


        /*if (imageLoader == null)
            imageLoader = Controller.getPermission().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);*/

        TextView notice_attachment = convertView.findViewById(R.id.notice_attachment);
        // Button notice_download = convertView.findViewById(R.id.notice_download);

        //TextView year = (TextView) convertView.findViewById(R.id.inYear);
        DataSet dataSet = DataList.get(position);

        notice_attachment.setText(dataSet.getnotice_attachment());
        Log.d("msg", notice_attachment.toString());
        //thumbNail.setImageUrl("http://androidlearnings.com/school/28_01481194918.jpg", imageLoader);
        // source.setText(String.valueOf(m.getSource()));
        // year.setText(String.valueOf(m.getYear()));

        return convertView;

    }

}
