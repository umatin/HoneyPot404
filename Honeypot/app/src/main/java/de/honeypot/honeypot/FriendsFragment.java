package de.honeypot.honeypot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import de.honeypot.honeypot.handlers.CircularImage;
import de.honeypot.honeypot.handlers.NetworkAdapter;

import static de.honeypot.honeypot.handlers.StorageHandler.readFileToBitmap;

public class FriendsFragment extends Fragment {
    private static final Logger logger = Logger.getLogger("FriendsFragment");
    private ListView friendlist;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser)
            updateData();
    }

    public void updateData() {
        NetworkAdapter.Profile base = null;
        if (getActivity().getIntent() != null) {
            int profileID = getActivity().getIntent().getIntExtra("profile", -1);
            if (profileID != -1) {
                base = new NetworkAdapter.Profile(NetworkAdapter.getInstance(), profileID);
            }
        }

        if (base == null) base = new NetworkAdapter.Profile(NetworkAdapter.getInstance());

        new FetchFriendsTask().execute(base);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.activity_friends_fragment, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        friendlist = (ListView) view.findViewById(R.id.listView);
        friendlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NetworkAdapter.Profile item = (NetworkAdapter.Profile) adapterView.getItemAtPosition(i);
                getActivity().getIntent().putExtra("profile", item.getID());
                updateData();
            }
        });
        updateData();
    }

    private class FetchFriendsTask extends AsyncTask<NetworkAdapter.Profile, Void, NetworkAdapter.Profile[]> {
        public NetworkAdapter.Profile[] doInBackground(NetworkAdapter.Profile... source) {
            if (source[0].getID() == -1) source[0] = NetworkAdapter.getInstance().getOwnProfile();
            return source[0].getFriends();
        }

        public void onPostExecute(NetworkAdapter.Profile[] profiles) {
            logger.info("Fetched profiles with length " + profiles.length);
            friendlist.setAdapter(new ProfileAdapter(getContext(), profiles));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.refresh:
                updateData();
                break;
        }

        return false;
    }
}

class ProfileAdapter extends BaseAdapter {

    Context context;
    NetworkAdapter.Profile[] data;

    private static LayoutInflater inflater = null;

    public ProfileAdapter(Context context, NetworkAdapter.Profile[] data) {
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
        name.setText(data[position].getName());
        score.setText(data[position].getPoints() + " points");
        meetcount.setText(data[position].getFriendsCount() + " friends");

        return vi;
    }
}
