package com.eraldguri.geophysicslab.fragments.tabs;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
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
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
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
import com.eraldguri.geophysicslab.util.RealPathUtil;
import com.eraldguri.geophysicslab.util.StringUtils;
import com.github.ybq.android.spinkit.SpinKitView;
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
    private SpinKitView spinKitView;
    private TextView tvLoading;

    private EarthquakeListAdapter mEarthquakeListAdapter;
    private FrameLayout mFrameLayout;
    private List<Features> earthquakes;

    private PermissionUtil permissionUtil;
    private boolean isDirectoryCreated;
    private final boolean exists = false;
    private static final int SNACK_BAR_DURATION = 5000;
    private boolean success = false;
    private File file;

    // dialog
    private TextView edtFilenameDialog;

    private String filename;

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
        spinKitView = view.findViewById(R.id.spin_kit);
        tvLoading = view.findViewById(R.id.tvLoading);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void openFileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflate = requireActivity().getLayoutInflater();
        View dialogView = inflate.inflate(R.layout.file_creator_dialog, null);

        edtFilenameDialog       = dialogView.findViewById(R.id.edt_filename_dialog);
        Button saveFileButton   = dialogView.findViewById(R.id.file_dialog_save_button);
        Button cancelFileButton = dialogView.findViewById(R.id.file_dialog_cancel_button);

        AlertDialog dialog = builder.setView(dialogView).create();

        saveFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filename = edtFilenameDialog.getText().toString();
                createFile();
                if (file.exists() && file.getName().equals(filename + StringUtils.csvExtension)) {
                    GeoSnackBar.errorSnackBar(requireContext(), dialogView, "File " + filename + " exists", 3000);
                } else {
                    exportToCSV(earthquakes);
                    dialog.dismiss();
                    GeoSnackBar.successSnackBar(requireContext(), mFrameLayout, R.string.csv_created, 3000);
                    Intent openFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + File.separator
                            + StringUtils.csvDirectory + File.separator);
                    openFileIntent.setDataAndType(uri, "text/csv");
                    requireActivity().startActivityForResult(openFileIntent, 100);
                }
            }
        });

        cancelFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void createFile() {
        File root = Environment.getExternalStorageDirectory();
        File directory = new File(root.getAbsolutePath() + "/" + StringUtils.csvDirectory);
        if (!directory.exists() && !isDirectoryCreated) {
            isDirectoryCreated = directory.mkdirs();
        }
        file = new File(directory, filename + StringUtils.csvExtension);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void exportToCSV(List<Features> features) {
       try {
           FileWriter outputFile = new FileWriter(file);
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
                       formattedTime, String.valueOf(magnitude),
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

       } catch (IOException e) {
           e.printStackTrace();
       }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mEarthquakeListAdapter != null) {
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
                String[] texts = new String[]{"6.", "7.", "8."};
                for (String s : texts) {
                    if (text.contains(s)) {
                        if (Character.isDigit(texts[0].charAt(0))) {
                            mEarthquakeListAdapter.getFilter().filter(text);
                        }
                    }
                }
            } else if (item.getItemId() == R.id.csv_export) {
                return checkForStoragePermissions();
            }
        }

        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean checkForStoragePermissions() {
        if (permissionUtil.checkPermission(READ_EXTERNAL_STORAGE)
                && permissionUtil.checkPermission(WRITE_EXTERNAL_STORAGE)) {
            openFileDialog();
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
            openFileDialog();
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

            spinKitView.setVisibility(View.INVISIBLE);
            tvLoading.setVisibility(View.INVISIBLE);

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