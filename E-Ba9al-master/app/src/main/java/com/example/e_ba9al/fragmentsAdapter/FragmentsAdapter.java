package com.example.e_ba9al.fragmentsAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class FragmentsAdapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> listeFragments = new ArrayList<>();
    private final ArrayList<String> titresFragments = new ArrayList<>();

    public FragmentsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return listeFragments.get(position);
    }

    @Override
    public int getCount() {
        return listeFragments.size();
    }

    public void addFragment(Fragment fragment, String titre){
        listeFragments.add(fragment);
        titresFragments.add(titre);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titresFragments.get(position);
    }
}
