package de.honeypot.honeypot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import de.honeypot.honeypot.handlers.CircularImage;

import static de.honeypot.honeypot.handlers.StorageHandler.readFileToBitmap;

public class FriendsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.activity_friends_fragment, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(new yourAdapter(getActivity(), new String[]{"data1",
                "data2"}));
        //TODO add listView items
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.friends_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.refresh:

                return true;
        }

        return false;
    }
}

class yourAdapter extends BaseAdapter {

    Context context;
    String[] data;

    private static LayoutInflater inflater = null;

    public yourAdapter(Context context, String[] data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.lv_item_friend, null);

        TextView name = (TextView) vi.findViewById(R.id.textView3);
        TextView score = (TextView) vi.findViewById(R.id.textView4);
        TextView meetcount = (TextView) vi.findViewById(R.id.textView5);
        ImageView imageView = (ImageView) vi.findViewById(R.id.imageView2);

        // TODO make following line access correct bitmap
        Bitmap bitmap = readFileToBitmap("iconUser.png");
        int res = CircularImage.relativeImageRes2(context);
        if (bitmap != null) {
            bitmap = CircularImage.circularProfilePicture(bitmap, res, true);
        } else {
            int color = Color.parseColor("#ff4081");
            Bitmap colorBitmap = Bitmap.createBitmap(res, res, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(colorBitmap);
            canvas.drawColor(color);

            bitmap = CircularImage.circularProfilePicture(colorBitmap, res, true);
        }

        try {
            imageView.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        // TODO set correct values
        name.setText(vi.getResources().getString(R.string.name) + ": " + data[position]);
        score.setText(vi.getResources().getString(R.string.score) + ": " + data[position]);
        meetcount.setText(vi.getResources().getString(R.string.meet_count) + ": " + data[position]);

        return vi;
    }
}
