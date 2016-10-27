package com.hsr.hemantsingh.insta.Activities;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.hsr.hemantsingh.insta.Models.ImageData;
import com.hsr.hemantsingh.insta.Models.User;
import com.hsr.hemantsingh.insta.MyApplication;
import com.hsr.hemantsingh.insta.R;
import com.hsr.hemantsingh.insta.playpause.PlayPauseView;
import com.squareup.picasso.Picasso;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageTabsActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private String userId;
    TextView imageTitleTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_tabs);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userId = getIntent().getStringExtra("id");
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        final Realm realm = Realm.getInstance(realmConfiguration);
        imageTitleTV = (TextView) findViewById(R.id.textView);


        setTitle(realm.where(User.class).equalTo("id", userId).findAll().first().getItems().first().getUser().getFull_name());
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), realm.where(User.class).equalTo("id", userId).findAll().first().getItems());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(getIntent().getIntExtra("index", 0));
//        mViewPager.setOffscreenPageLimit(3);
//        mViewPager.setPageTransformer(true, new CubeOutTransformer());
          mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
              @Override
              public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

              }

              @Override
              public void onPageSelected(int position) {

                  imageTitleTV.setText(realm.where(User.class).equalTo("id", userId).findAll().first().getItems().get(position).getCaption() != null ? realm.where(User.class).equalTo("id", userId).findAll().first().getItems().get(position).getCaption().getText() : "");
              }

              @Override
              public void onPageScrollStateChanged(int state) {

              }
          });

    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_SECTION_URL = "section_url";
        private static  final  String ARG_SECTION_TITLE = "section_title";
        VideoView vv;
        PlayPauseView fab;

        public PlaceholderFragment() {

        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, String url, String title) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_SECTION_URL, url);
            args.putString(ARG_SECTION_TITLE, title);
            fragment.setArguments(args);

            return fragment;
        }

        public void setUserVisibleHint(boolean isVisibleToUser)
        {
            super.setUserVisibleHint(isVisibleToUser);
            if (this.isVisible())
            {
                if (!isVisibleToUser)   // If we are becoming invisible, then...
                {
                    //pause or stop video
                    vv.pause();
//                    fab.toggle();
                }
                else {
                    vv.start();
                }

            }
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_image_tabs, container, false);
//            TextView titleTV = (TextView) rootView.findViewById(R.id.textView);
            vv = (VideoView) rootView.findViewById(R.id.videoView);
            final PhotoViewAttacher mAttacher;
            final ImageView imageView = (ImageView) rootView.findViewById(R.id.ivProfile);
             fab = (PlayPauseView) rootView.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (vv.isPlaying()){
                       vv.pause();
                        fab.toggle();
                    }
                    else {
                        vv.start();
                        fab.toggle();
                    }
                }
            });
            imageView.setMinimumHeight(MyApplication.getInstance().getMetrics().widthPixels);
            if (this.getArguments().getString(ARG_SECTION_URL).contains(".jpg")){
                vv.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
                mAttacher = new PhotoViewAttacher(imageView);
//                imageView.setImageBitmap(AltexImageDownloader.readFromDisk(new File(this.getArguments().getString(ARG_SECTION_URL))));
//                mAttacher.update();
                 Picasso.with(getContext()).load(new File(this.getArguments().getString(ARG_SECTION_URL))).fit().into(imageView);
//                AltexImageDownloader.readFromDiskAsync(new File(this.getArguments().getString(ARG_SECTION_URL)), new AltexImageDownloader.OnImageReadListener() {
//                    @Override
//                    public void onImageRead(Bitmap bitmap) {
//                        imageView.setImageBitmap(bitmap);
//                        mAttacher.update();
//                    }
//
//                    @Override
//                    public void onReadFailed() {
//                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                         imageView.setImageResource(R.mipmap.ic_launcher);
//                          mAttacher.update();
//                    }
//                });

            }
            else {
                imageView.setVisibility(View.GONE);
                vv.setVideoPath(this.getArguments().getString(ARG_SECTION_URL));

            }
//             titleTV.setText(getArguments().getString(ARG_SECTION_TITLE));
            return rootView;
        }

        @Override
        public void onDetach() {
            super.onDetach();
            vv.stopPlayback();
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public RealmList<ImageData> img;
        public String[] imgNameList;

        public SectionsPagerAdapter(FragmentManager fm, RealmList<ImageData> data) {
            super(fm);
            this.img = data;
            File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath()
                    + "/" + img.first().getUser().getUsername() +"/");
            imgNameList =  folder.list();


        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).


            return PlaceholderFragment.newInstance(position ,Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath()
                    + "/" + img.first().getUser().getUsername() +"/" + imgNameList[position], (   position < img.size() && img.get(position).getCaption() != null)? img.get(position).getCaption().getText().toString() : "");
        }

        @Override
        public int getCount() {
            // Show 3 total pages

            return imgNameList.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return "";
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}