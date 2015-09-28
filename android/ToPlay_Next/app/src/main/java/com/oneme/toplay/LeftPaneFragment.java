package com.oneme.toplay;

public class LeftPaneFragment { //extends Fragment {
    /*

    public class LeftPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = { getString(R.string.titles_recent),
                getString(R.string.titles_contacts), getString(R.string.titles_settings) };

        public LeftPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

            case 0: return new MessageFragment();
            case 1: return new ContactsFragment();
            case 2: return new SettingsFragment();
            default: return new MessageFragment();
            }
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }
    }
    private MainActivity main_act;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //
        //  Stores a 2 dimensional string array holding friend details. Will be populated
        //  by a function once implemented
        //
        main_act = (MainActivity) getActivity();

        View rootView = inflater.inflate(R.layout.fragment_leftpane, container, false);

        // Initialize the ViewPager and set an adapter
        ViewPager pager = (ViewPager) rootView.findViewById(R.id.pager);
        pager.setAdapter(new LeftPagerAdapter(getFragmentManager()));


        //Ozzie Zhang 2014-12-08 disable this code

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        return rootView;
    }
    */
}
