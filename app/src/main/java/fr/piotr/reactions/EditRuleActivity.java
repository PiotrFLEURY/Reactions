package fr.piotr.reactions;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.UUID;

import fr.piotr.reactions.builder.RuleBuilder;
import fr.piotr.reactions.dialogs.TimePickerFragment;
import fr.piotr.reactions.dialogs.VibratorPatternPicker;
import fr.piotr.reactions.dialogs.WallpaperPicker;
import fr.piotr.reactions.events.time.HourMinute;
import fr.piotr.reactions.persistence.FlatRule;
import fr.piotr.reactions.registry.EventsRegistry;
import fr.piotr.reactions.registry.ReactionsRegistry;
import fr.piotr.reactions.services.ReactionsService;
import fr.piotr.reactions.utils.FlatRuleConverter;
import fr.piotr.reactions.utils.PatternConverter;

public class EditRuleActivity extends AppCompatActivity {

    public static final int LOCATION_PERMISSION_REQUEST = 1;
    public static final int RINGTONE_PICKING = 2;
    public static final int SETTINGS_PERMISSION_REQUEST = 3;
    public static final int READ_STORAGE_PERMISSION_REQUEST = 4;
//    public static final int WRITE_STORAGE_PERMISSION_REQUEST = 5;
    public static final int GET_ACCOUNTS_PERMISSION_REQUEST = 6;

    public static final int SELECT_LOCATION_REQUEST = 7;

    public static final int READ_SMS_PERMISSION_REQUEST = 8;
    public static final int WRITE_SMS_PERMISSION_REQUEST = 9;

    public static final String EVENT_WALLPAPER_PICKED_UP = "EVENT_WALLPAPER_PICKED_UPD";
    public static final String EXTRA_WALLPAPER_ID = "EXTRA_WALLPAPER_ID";

//    public static final String EVENT_LOCATION_PICKED_UP = "EVENT_LOCATION_PICKED_UP";
//    public static final String EXTRA_LOCATION_ADDRESS = "EXTRA_LOCATION_ADDRESS";

    public static final String EVENT_TIME_PICKED_UP = "EVENT_TIME_PICKED_UP";
    public static final String EXTRA_TIME = "EXTRA_TIME";

    public static final String SELECT_EVENT = "SELECT_EVENT";
    public static final String EXTRA_EVENT = "EXTRA_EVENT";

    public static final String SELECT_REACTION = "SELECT_REACTION";
    public static final String EXTRA_REACTION = "EXTRA_REACTION";

    IntentFilter intentFilter = new IntentFilter() {{
        addAction(EVENT_WALLPAPER_PICKED_UP);
//        addAction(EVENT_LOCATION_PICKED_UP);
        addAction(SELECT_EVENT);
        addAction(SELECT_REACTION);
        addAction(EVENT_TIME_PICKED_UP);
    }};

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case EVENT_WALLPAPER_PICKED_UP:
                    builder.setPickedImageId(intent.getIntExtra(EXTRA_WALLPAPER_ID, -1));
                    break;
//                case EVENT_LOCATION_PICKED_UP:
//                    onAddressPickedUp(intent.getParcelableExtra(EXTRA_LOCATION_ADDRESS));
//                    break;
                case EVENT_TIME_PICKED_UP:
                    builder.setPickedHourMinute((HourMinute) intent.getSerializableExtra(EXTRA_TIME));
                    break;
                case SELECT_EVENT:
                    gotoPage2();
                    builder.setEventsRegistry((EventsRegistry) intent.getSerializableExtra(EXTRA_EVENT));
                    getEventData(builder.getEventsRegistry());
                    break;
                case SELECT_REACTION:
                    builder.setReactionsRegistry((ReactionsRegistry) intent.getSerializableExtra(EXTRA_REACTION));
                    getReactionData(builder.getReactionsRegistry());
                    break;
            }
        }
    };

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
    private FloatingActionButton fab;

    private RuleBuilder builder;

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rule);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        PageChangeListener pageChangeListener = new PageChangeListener();
        mViewPager.addOnPageChangeListener(pageChangeListener);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_next);
        fab.setOnClickListener(view -> onFlatButtonClick(view, pageChangeListener));

        builder=new RuleBuilder(EditRuleActivity.this);

    }

    private void onFlatButtonClick(View view, PageChangeListener pageChangeListener) {
        if(pageChangeListener.getCurrentPage()==0) {
            gotoPage2();
        } else if(!builder.isEventValid()) {
            gotoPage1();
            Snackbar.make(view, R.string.choose_event, Snackbar.LENGTH_LONG).show();
        } else if(!builder.isReactionValid()) {
            Snackbar.make(view, R.string.choose_reaction, Snackbar.LENGTH_LONG).show();
        } else {
            onValidateRule();
        }
    }

    private void gotoPage1() {
        mViewPager.setCurrentItem(0);
    }

    private void onValidateRule() {
        EventsRegistry eventsRegistry = builder.getEventsRegistry();
        ReactionsRegistry reactionsRegistry = builder.getReactionsRegistry();
        FlatRule flatRule = new FlatRule(eventsRegistry.getEventId(),
                getEventExtra(eventsRegistry),
                reactionsRegistry.getReactionsId(),
                getReactionExtra(reactionsRegistry));

        Rule rule = FlatRuleConverter.buildRule(this, flatRule);
        ReactionsApplication.getReactionsManager().add(rule);
        if(ReactionsService.running.get()) {
            rule.activate();
        }
        finish();
    }

    private void getReactionData(ReactionsRegistry reactionsRegistry) {
        switch(reactionsRegistry){
            case CHANGE_RINGTONE:
                if(!requestWriteSettingsPermission()) {
                    pickupRingtone();
                }
                break;
            case SET_WALLPAPER:
                pickupWallpaper();
                break;
            case VIBRATE:
                pickupPattern();
                break;
            case EMAIL:
                pickupMailMessage();
                break;
            case SHARE_POSITION:
                pickupPhoneNumber();
                break;
            default:
                break;
        }
    }

    private void pickupPhoneNumber() {
        if(!requestWriteSmsPermission()) {
            EditText etMessageText = new EditText(this);
            etMessageText.setInputType(InputType.TYPE_CLASS_PHONE);
            new AlertDialog.Builder(this).setView(etMessageText).setPositiveButton(R.string.validate, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Editable text = etMessageText.getText();
                    if (!text.toString().isEmpty()) {
                        builder.setPickedPhoneNumber(text.toString());
                        dialogInterface.dismiss();
                    }
                }
            }).show();
        }
    }

    private void pickupMailMessage() {
        if(!requestGetAccountPermission()){
            EditText message = new EditText(this);
            new AlertDialog.Builder(this).setView(message).setPositiveButton(R.string.validate, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    builder.setPickedMailMessage(message.getText().toString());
                }
            }).create().show();
        }
    }

    private boolean requestGetAccountPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.GET_ACCOUNTS}, GET_ACCOUNTS_PERMISSION_REQUEST);
            return true;
        }
        return false;
    }

    private void pickupPattern() {
        VibratorPatternPicker vibratorPatternPicker = new VibratorPatternPicker(this);
        vibratorPatternPicker.setOnDismissListener(dialogInterface -> builder.setPickedPattern(vibratorPatternPicker.getPattern()));
        vibratorPatternPicker.show();
    }

    @Nullable
    private void getEventData(EventsRegistry eventsRegistry) {
        switch(eventsRegistry){
            case POSITION:
                pickupLocation();
                break;
            case TIME:
                pickupTime();
                break;
            case SMS:
                pickupText();
                break;
            default:
                break;
        }
    }

    private void pickupText() {
        if(!requestReadSmsPermission()) {
            EditText etMessageText = new EditText(this);
            new AlertDialog.Builder(this).setView(etMessageText).setPositiveButton(R.string.validate, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Editable text = etMessageText.getText();
                    if (!text.toString().isEmpty()) {
                        builder.setPickedMessageText(text.toString());
                        dialogInterface.dismiss();
                    }
                }
            }).show();
        }
    }

    private void pickupTime() {
        new TimePickerFragment().show(getFragmentManager(), "timePicker");
    }

    private void pickupLocation() {
        Intent intent = new Intent(this, AddressListActivity.class);
        intent.putExtra(AddressListActivity.INTENT_MODE, AddressListActivity.MODE_SELECTION);
        startActivityForResult(intent, SELECT_LOCATION_REQUEST);
//        if(!requestLocationPermission()) {
//
//            LocationPicker locationPicker = new LocationPicker(this);
//            locationPicker.show();
//
//        }
    }

//    private void onAddressPickedUp(Address address) {
//        builder.setPickedAddress(ReactionsApplication.getAddressManager().add(address));
////        Snackbar.make(fab, LocationConverter.fromAddress(address), Snackbar.LENGTH_LONG).show();
//    }

    private boolean requestReadSmsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)) {

            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, READ_SMS_PERMISSION_REQUEST);
            return true;
        }
        return false;
    }

    private boolean requestWriteSmsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, WRITE_SMS_PERMISSION_REQUEST);
            return true;
        }
        return false;
    }

//    private boolean requestLocationPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST);
//            return true;
//        }
//        return false;
//    }

    private String getEventExtra(EventsRegistry eventsRegistry) {
        switch (eventsRegistry) {
            case POSITION:
                return builder.getPickedAddress().getId().toString();
            case TIME:
                return builder.getPickedHourMinute().toString();
            case SMS:
                return builder.getPickedMessageText();
            default:
                return eventsRegistry.getExtra();
        }
    }

    private String getReactionExtra(ReactionsRegistry reactionsRegistry) {
        switch (reactionsRegistry){
            case CHANGE_RINGTONE:
                return builder.getPickedRingtone().toString();
            case SET_WALLPAPER:
                return ""+builder.getPickedImageId();
            case VIBRATE:
                return PatternConverter.asString(builder.getPickedPattern());
            case EMAIL:
                return builder.getPickedMailMessage();
            case SHARE_POSITION:
                return builder.getPickedPhoneNumber();
            default:return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RINGTONE_PICKING
                && data!=null){
            Uri ringtone = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if(ringtone!=null) {
                builder.setPickedRingtone(ringtone);
            }
        } else if(requestCode==SELECT_LOCATION_REQUEST && data!=null){
            String locationUUID = data.getStringExtra(AddressListActivity.EXTRA_LOCATION_UUID);
            UUID addressId = UUID.fromString(locationUUID);
            builder.setPickedAddress(ReactionsApplication.getAddressManager().get(addressId));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==SETTINGS_PERMISSION_REQUEST
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED){
            pickupRingtone();
        }
        if(requestCode==READ_STORAGE_PERMISSION_REQUEST
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            pickupWallpaper();
        }
        if(requestCode==LOCATION_PERMISSION_REQUEST
                && (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)){
            pickupLocation();
        }
        if(requestCode==GET_ACCOUNTS_PERMISSION_REQUEST
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED){
            pickupMailMessage();
        }
        if(requestCode==READ_SMS_PERMISSION_REQUEST
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED){
            pickupText();
        }
        if(requestCode==WRITE_SMS_PERMISSION_REQUEST
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            pickupPhoneNumber();
        }
    }

    private void pickupRingtone() {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, getString(R.string.pickup_ringtone));
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
        startActivityForResult(intent, RINGTONE_PICKING);
    }

    private void pickupWallpaper() {

        if(requestReadStoragePermission()){
            return;
        }

        new WallpaperPicker(this).show();
    }

    private boolean requestReadStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_PERMISSION_REQUEST);
            return true;
        }
        return false;
    }

    private boolean requestWriteSettingsPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(this)) {
                return false;
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            }
        } else {
            return false;
        }

    }

    public void onPageChanged(int position){
        if(position==0){
            fab.setImageResource(R.drawable.ic_next);
        } else {
            fab.setImageResource(R.drawable.ic_check);
        }
    }

    public class PageChangeListener implements ViewPager.OnPageChangeListener {

        int currentPage=0;

        public int getCurrentPage() {
            return currentPage;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //
        }

        @Override
        public void onPageSelected(int position) {
            currentPage=position;
            onPageChanged(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //
        }
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

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_edit_rule, container, false);
            TextView title = (TextView) rootView.findViewById(R.id.edit_rule_page_title);
            GridView grid = (GridView) rootView.findViewById(R.id.edit_rule_grid);
            if(getArguments().getInt(ARG_SECTION_NUMBER)==1){
                title.setText(R.string.events);
                grid.setAdapter(new RuleEventAdapter());
            } else {
                title.setText(R.string.reactions);
                grid.setAdapter(new RuleReactionAdapter());
            }
            return rootView;
        }
    }

    private static class RuleEventAdapter extends BaseAdapter {

        private FrameLayout lastView;
        private EventsRegistry[] events = EventsRegistry.values();

        @Override
        public int getCount() {
            return events.length;
        }

        @Override
        public EventsRegistry getItem(int i) {
            return events[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            EventsRegistry item = getItem(i);
            Context context = viewGroup.getContext();
            if(view==null) {
                view = LayoutInflater.from(context).inflate(R.layout.rule_grid_item, null, false);
            }
            view.setOnClickListener(view1 -> select((FrameLayout) view1, item));

            ImageView icRuleItem = (ImageView) view.findViewById(R.id.icon_rule_item);
            icRuleItem.setImageResource(item.getIcon());

            TextView tvRuleItem = (TextView) view.findViewById(R.id.text_rule_item);
            tvRuleItem.setText(context.getString(item.getLabel()));
            return view;
        }

        private void select(FrameLayout view, EventsRegistry item) {
            if(lastView!=null) {
                lastView.findViewById(R.id.checked_rule_item).setVisibility(View.INVISIBLE);
                lastView.animate().scaleX(1f).scaleY(1f);
            }
            view.findViewById(R.id.checked_rule_item).setVisibility(View.VISIBLE);
            view.animate().scaleX(0.9f).scaleY(0.9f);
            lastView=view;

            Intent intent = new Intent(SELECT_EVENT);
            intent.putExtra(EXTRA_EVENT, item);
            LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);

        }

    }

    private void gotoPage2() {
        mViewPager.setCurrentItem(1);
    }

    private static class RuleReactionAdapter extends BaseAdapter {

        private FrameLayout lastView;
        private ReactionsRegistry[] events = ReactionsRegistry.values();

        @Override
        public int getCount() {
            return events.length;
        }

        @Override
        public ReactionsRegistry getItem(int i) {
            return events[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ReactionsRegistry item = getItem(i);
            Context context = viewGroup.getContext();
            if(view==null) {
                view = LayoutInflater.from(context).inflate(R.layout.rule_grid_item, null, false);
            }
            view.setOnClickListener(view1 -> select((FrameLayout) view1, item));

            ImageView icRuleItem = (ImageView) view.findViewById(R.id.icon_rule_item);
            icRuleItem.setImageResource(item.getIcon());

            TextView tvRuleItem = (TextView) view.findViewById(R.id.text_rule_item);
            tvRuleItem.setText(context.getString(item.getLabel()));
            return view;
        }

        private void select(FrameLayout view, ReactionsRegistry item) {
            if(lastView!=null) {
                lastView.findViewById(R.id.checked_rule_item).setVisibility(View.INVISIBLE);
                lastView.animate().scaleX(1f).scaleY(1f);
            }
            view.findViewById(R.id.checked_rule_item).setVisibility(View.VISIBLE);
            view.animate().scaleX(0.9f).scaleY(0.9f);
            lastView=view;
            Intent intent = new Intent(SELECT_REACTION);
            intent.putExtra(EXTRA_REACTION, item);
            LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getApplicationContext().getString(R.string.events);
                case 1:
                    return getApplicationContext().getString(R.string.reactions);
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        if(mViewPager.getCurrentItem()==1){
            gotoPage1();
            return;
        }
        super.onBackPressed();
    }
}
