package com.example.eklavya.ui.adminusers;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.eklavya.R;
import com.example.eklavya.userfaculty;
import com.example.eklavya.userstudent;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class AdminUsersFragment extends Fragment {


    private AdminUsersViewModel mViewModel;


    private userfaculty UF;
    private userstudent US;


    TabLayout TabL;
    private ViewPager viewPager;

    public static AdminUsersFragment newInstance() {
        return new AdminUsersFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.admin_users_fragment, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AdminUsersViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
      viewPager=view.findViewById(R.id.view_pager);
      TabL=view.findViewById(R.id.tab_layout);


      UF=new userfaculty();
      US=new userstudent();

      TabL.setupWithViewPager(viewPager);


      ViewPagerAdapter viewPagerAdapter= new ViewPagerAdapter(getParentFragmentManager());
       viewPagerAdapter.addFragment(US,"Student");
      viewPagerAdapter.addFragment(UF,"Faculty");

      viewPager.setAdapter(viewPagerAdapter);


    }


   private  class ViewPagerAdapter extends FragmentPagerAdapter{

        private List<Fragment> fragmentList=new ArrayList<>();
        private List<String>fragmentTitle=new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        public  void addFragment(Fragment fragment, String title){
            fragmentList.add(fragment);
            fragmentTitle.add(title);
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {

            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

       @Nullable
       @Override
       public CharSequence getPageTitle(int position) {
           return fragmentTitle.get(position);
       }
   }
}
