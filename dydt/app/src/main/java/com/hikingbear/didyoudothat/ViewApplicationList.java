package com.hikingbear.didyoudothat;



import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hikingbear.appdata.ApkAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewApplicationList extends Activity implements AdapterView.OnItemClickListener {

    PackageManager packageManager;
    ListView apkList;
    public Bundle dataBundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewapplist);

        packageManager = getPackageManager();
        List<PackageInfo> packageList = packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS);

        List<PackageInfo> packageList1 = new ArrayList<PackageInfo>();

        /*To filter out System apps*/
        for(PackageInfo pi : packageList) {
            boolean b = isSystemPackage(pi);
            if(!b) {
                packageList1.add(pi);
            }
        }
        apkList = (ListView) findViewById(R.id.applist);
        apkList.setAdapter(new ApkAdapter(this, packageList1, packageManager));

        apkList.setOnItemClickListener(this);
    }

    /**
     * Return whether the given PackgeInfo represents a system package or not.
     * User-installed packages (Market or otherwise) should not be denoted as
     * system packages.
     *
     * @param pkgInfo
     * @return boolean
     */

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true: false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long row) {
        PackageInfo packageInfo = (PackageInfo) parent.getItemAtPosition(position);

        //appData.setPackageInfo(packageInfo);

        //Toast.makeText(this, "어플이 선택됐네. 뭔지 체크는 어떻게하는거지?" + packageInfo, Toast.LENGTH_SHORT).show();
        //Intent appInfo = new Intent(getApplicationContext(), ApkInfo.class);
        //startActivity(appInfo);
        try {

            Toast.makeText(this, "선택한 어플: " + packageInfo.packageName, Toast.LENGTH_SHORT).show();

            //Intent intent = packageManager.getLaunchIntentForPackage(packageInfo.packageName);
            //if(intent != null) {
            //    startActivity(intent);
            //}
            dataBundle = new Bundle();
            dataBundle.putString("app", packageInfo.packageName);

            Intent intent = new Intent(getApplicationContext(), AddScheduleActivity.class);
            intent.putExtras(dataBundle);
            startActivity(intent);
            finish();

        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

}
