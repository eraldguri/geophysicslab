package com.eraldguri.geophysicslab.fragments.tabs;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.eraldguri.geophysicslab.adapter.EarthquakeListAdapter;
import com.eraldguri.geophysicslab.R;
import com.eraldguri.geophysicslab.api.model.Features;
import com.eraldguri.geophysicslab.api.retrofit.ApiViewModel;
import com.eraldguri.geophysicslab.fragments.EarthquakeFragment;
import com.eraldguri.geophysicslab.navigation.EarthquakesFragment;
import com.eraldguri.geophysicslab.permissions.PermissionCallback;
import com.eraldguri.geophysicslab.permissions.PermissionUtil;
import com.eraldguri.geophysicslab.util.DateTimeUtil;
import com.eraldguri.geophysicslab.util.DividerItemDecorator;
import com.eraldguri.geophysicslab.util.GeoSnackBar;
import com.eraldguri.geophysicslab.util.StringUtils;
import com.google.android.material.snackbar.Snackbar;
import com.opencsv.CSVWriter;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

@RequiresApi(api = Build.VERSION_CODES.M)
public class EarthquakeListFragment extends EarthquakesFragment implements
        EarthquakeListAdapter.OnItemClickListener {

    private RecyclerView mEarthquakeListView;
    private EarthquakeListAdapter mEarthquakeListAdapter;
    private FrameLayout mFrameLayout;
    private List<Features> earthquakes;

    private PermissionUtil permissionUtil;
    private boolean isDirectoryCreated;
    private boolean isCSVCreated;
    private static final int SNACK_BAR_DURATION = 5000;
    private boolean success = false;

    int counter = 0;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_earthquake_list, container, false);

        initViews(root);
        setHasOptionsMenu(true);

        permissionUtil = new PermissionUtil(requireContext());

        ApiViewModel viewModel = new ViewModelProvider(requireActivity()).get(ApiViewModel.class);
        viewModel.getFeatures().observe(getViewLifecycleOwner(), this::setupRecyclerView);

        return root;
    }

    private void initViews(View view) {
        mEarthquakeListView = view.findViewById(R.id.rv_earthquake_data_list);
        mFrameLayout = view.findViewById(R.id.earthquake_list_container);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void exportToCSV(List<Features> features) {
       File root = Environment.getExternalStorageDirectory();
       File dir = new File(root.getAbsolutePath() + StringUtils.csvDirectory);
       if (!dir.exists() && !isDirectoryCreated) {
           isDirectoryCreated = dir.mkdirs();
       }
       File csvFile = new File(dir, StringUtils.csvName + StringUtils.csvExtension);
       if (csvFile.getName().equals(StringUtils.csvName + StringUtils.csvExtension)) {
           StringUtils.replaceWithNewFileName(StringUtils.csvName + StringUtils.csvExtension, dir, StringUtils.csvExtension);
           //GeoSnackBar.errorSnackBar(requireContext(), mFrameLayout, R.string.csv_exists, SNACK_BAR_DURATION);
           success = false;
       } else {
           try {
               FileWriter outputFile = new FileWriter(csvFile);
               CSVWriter writer = new CSVWriter(outputFile);
               writer.writeNext(StringUtils.header);

               for (int i = 0; i < features.size(); i++) {
                   double[] geometry = features.get(i).getGeometry().getCoordinates();
                   double longitude = geometry[0];
                   double latitude = geometry[1];
                   double depth = geometry[2];

                   String place = features.get(i).getProperties().getPlace();
                   double magnitude = features.get(i).getProperties().getMagnitude();
                   String felt = features.get(i).getProperties().getFelt();

                   String dateTime = features.get(i).getProperties().getTime();
                   String formattedTime = DateTimeUtil.parseDateTimeFromString(dateTime);

                   int tsunami = features.get(i).getProperties().getTsunami();
                   int tz = features.get(i).getProperties().getTz();
                   String cdi = features.get(i).getProperties().getCdi();
                   String mmi = features.get(i).getProperties().getMmi();
                   int sig = features.get(i).getProperties().getSig();
                   int nst = features.get(i).getProperties().getNst();
                   double dmin = features.get(i).getProperties().getDmin();
                   double rms = features.get(i).getProperties().getRms();
                   double gap = features.get(i).getProperties().getGap();
                   String magType = features.get(i).getProperties().getMagType();

                   String _tsunami = " ";
                   if (tsunami == 1) {
                       _tsunami = "Yes";
                   } else if (tsunami == 0) {
                       _tsunami = " ";
                   }
                   writer.writeNext(new String[]{
                           place,
                           felt,
                           formattedTime,
                           String.valueOf(magnitude),
                           String.valueOf(latitude),
                           String.valueOf(longitude),
                           String.valueOf(depth),
                           cdi,
                           mmi,
                           String.valueOf(sig),
                           magType,
                           String.valueOf(gap),
                           String.valueOf(rms),
                           String.valueOf(dmin),
                           String.valueOf(nst),
                           String.valueOf(tz),
                           magType,
                           _tsunami
                   });
               }

               writer.close();
               if (success) {
                   GeoSnackBar.successSnackBar(requireContext(), mFrameLayout, R.string.csv_created, SNACK_BAR_DURATION);
               } else {
                   GeoSnackBar.errorSnackBar(requireContext(), mFrameLayout, R.string.failed_to_export, SNACK_BAR_DURATION);
               }

           } catch (IOException e) {
               e.printStackTrace();
           }
       }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        assert mEarthquakeListAdapter != null;
        String text;
        if (item.getItemId() == R.id.magnitude_all) {
            mEarthquakeListAdapter.getFilter().filter("");
        } else if (item.getItemId() == R.id.magnitude_3) {
            text = "3.";
            StringUtils.isDigit(text, mEarthquakeListAdapter);
        } else if (item.getItemId() == R.id.magnitude_4) {
            text = "4.";
            StringUtils.isDigit(text, mEarthquakeListAdapter);
        } else if (item.getItemId() == R.id.magnitude_5) {
            text = "5.";
            StringUtils.isDigit(text, mEarthquakeListAdapter);
        } else if (item.getItemId() == R.id.magnitude_strong) {
            text = "    ";
            String[] texts = new String[] {"6.", "7.", "8."};
            for (String s: texts) {
                if (text.contains(s)) {
                    if (Character.isDigit(texts[0].charAt(0))) {
                        mEarthquakeListAdapter.getFilter().filter(text);
                    }
                }
            }
        } else if (item.getItemId() == R.id.csv_export) {
            return checkForStoragePermissions();
        }

        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean checkForStoragePermissions() {
        if (permissionUtil.checkPermission(READ_EXTERNAL_STORAGE)
                && permissionUtil.checkPermission(WRITE_EXTERNAL_STORAGE)) {
            exportToCSV(earthquakes);
        } else {
            if (permissionUtil.shouldShowRequestPermissionRationale(requireActivity(), WRITE_EXTERNAL_STORAGE)) {
                Snackbar.make(mFrameLayout,
                        "GeophysicsLab will need to write to external storage to export as CSV",
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", v ->
                                permissionUtil.askForPermission(requireActivity(),
                                        new String[] {
                                                READ_EXTERNAL_STORAGE,
                                                WRITE_EXTERNAL_STORAGE
                                        }, mPermissionCallback)).show();
            } else {
                permissionUtil.askForPermission(requireActivity(), new String[] {
                        READ_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE}, mPermissionCallback);
            }
        }

        return false;
    }

    private final PermissionCallback mPermissionCallback = new PermissionCallback() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void permissionGranted() {
            Toast.makeText(requireContext(), "Permissions granted", Toast.LENGTH_LONG).show();
            exportToCSV(earthquakes);
        }

        @Override
        public void permissionRefused() {
            Toast.makeText(requireContext(),
                    "To export as CSV you need to enable storage permissions", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupRecyclerView(List<Features> earthquakes) {
        if (mEarthquakeListAdapter == null) {
            mEarthquakeListAdapter = new EarthquakeListAdapter(getContext(), earthquakes, this);
            mEarthquakeListView.setLayoutManager(new LinearLayoutManager(requireContext()));
            mEarthquakeListView.addItemDecoration(new DividerItemDecorator(requireContext()));
            mEarthquakeListView.setAdapter(mEarthquakeListAdapter);

            this.earthquakes = earthquakes;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, List<Features> features) {
        Features quakes = features.get(position);

        FragmentManager fragmentManager = getChildFragmentManager();
        Fragment earthquakeFragment = new EarthquakeFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.earthquake_list_container, earthquakeFragment)
                .commit();

        double[] coordinates = quakes.getGeometry().getCoordinates();
        String dateTime = quakes.getProperties().getTime();
        String formattedTime = DateTimeUtil.parseDateTimeFromString(dateTime);

        Bundle bundle = new Bundle();
        bundle.putString("title", quakes.getProperties().getPlace());
        bundle.putDouble("magnitude", quakes.getProperties().getMagnitude());
        bundle.putDouble("latitude", coordinates[1]);
        bundle.putDouble("longitude", coordinates[0]);
        bundle.putDouble("depth", coordinates[2]);
        bundle.putString("date_time", formattedTime);
        bundle.putInt("tsunami", quakes.getProperties().getTsunami());
        bundle.putString("felt", quakes.getProperties().getFelt());
        bundle.putInt("tz", quakes.getProperties().getTz());
        bundle.putString("cdi", quakes.getProperties().getCdi());
        bundle.putString("mmi", quakes.getProperties().getMmi());
        bundle.putInt("sig", quakes.getProperties().getSig());
        bundle.putInt("nst", quakes.getProperties().getNst());
        bundle.putDouble("dmin", quakes.getProperties().getDmin());
        bundle.putDouble("rms",  quakes.getProperties().getRms());
        bundle.putDouble("gap", quakes.getProperties().getGap());
        bundle.putString("magType", quakes.getProperties().getMagType());

        earthquakeFragment.setArguments(bundle);
    }

}