package com.dam.appmarvel;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilmsFragment extends Fragment {

    private static int REQUEST_CODE = 100;
    ImageView imgChronology;
    Button btnSave;
    View view;

    public FilmsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_films, container, false);


        imgChronology = view.findViewById(R.id.img_chronology);
        btnSave = view.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    guardarImagen();
                } else {
                    preguntarPermisos();
                }
            }
        });
        return view;
    }

    private void guardarImagen() {
        File dir = new File(Environment.getDataDirectory(), "GuardarImagen");

        if(!dir.exists()){
            dir.mkdir();
        }


        BitmapDrawable drawable = (BitmapDrawable) imgChronology.getDrawable();
        Bitmap bitmap = drawable.getBitmap();


        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG,100, new FileOutputStream("/sdcard/chronology.jpg"));
            Toast.makeText(getActivity(), "Imagen guardada correctamente",Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void preguntarPermisos() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                guardarImagen();
            } else {
                Toast.makeText(getActivity(), "Proporcione el permiso requerido", Toast.LENGTH_LONG).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}