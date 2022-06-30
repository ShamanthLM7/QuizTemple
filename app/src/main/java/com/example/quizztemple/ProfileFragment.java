package com.example.quizztemple;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.quizztemple.databinding.FragmentProfileBinding;
import com.example.quizztemple.databinding.FragmentWalletBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.xml.transform.Result;


public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

     FragmentProfileBinding binding;
    FirebaseFirestore database;
    FirebaseAuth auth;
    FirebaseDatabase databasereal;
    Context context;
    FirebaseUser firebaseUser;
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri imageUri;

    User user;
    String em,pa,nam;
    ProgressDialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // binding = FragmentWalletBinding.inflate(inflater, container, false);
        binding = FragmentProfileBinding.inflate(inflater,container,false);
        database = FirebaseFirestore.getInstance();
        databasereal =FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        firebaseUser= auth.getCurrentUser();
        storage= FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Uploading Image");
        dialog.setCancelable(false);

        databasereal.getReference().child("image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String image = snapshot.getValue(String.class);
                Picasso.get().load(image).into(binding.propro);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
                binding.propro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosepic();
            }
        });
        binding.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isemailchanged()) {


                        firebaseUser.updateEmail(em).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(), "Done updating", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                            }
                        });

                        Toast.makeText(getContext(), "Done updating", Toast.LENGTH_SHORT).show();

                }
                if(ispasschanged())
                {


                        firebaseUser.updatePassword(pa).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(), "Done updating", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Toast.makeText(getContext(), "Done updating", Toast.LENGTH_SHORT).show();

                }
                if(isnamechanged())
                {
                    Toast.makeText(getContext(), "Done updating", Toast.LENGTH_SHORT).show();
                }





            }


        });

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(getContext(),LoginActivity.class));
            }
        });



        return binding.getRoot();
    }

    private void choosepic() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && data!=null && data.getData()!=null)
        {
            imageUri = data.getData();



            database.collection("users")
                    .document(FirebaseAuth.getInstance().getUid())
                    .update("profile",imageUri).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    //Toast.makeText(getContext(), "wow", Toast.LENGTH_SHORT).show();

                }
            });
            binding.propro.setImageURI(imageUri);


                uploadpic();

        }
    }

    private void uploadpic()  {
/*        // Create a reference to "mountains.jpg"
        String randomkey = UUID.randomUUID().toString();
        StorageReference mountainsRef = storageReference.child("images/"+randomkey);

        mountainsRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                    }
                });

        File localfile = File.createTempFile(randomkey,"jpeg");
        storageReference.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                binding.propro.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error file", Toast.LENGTH_SHORT).show();
            }
        });*/
        StorageReference reference = storage.getReference().child("image");
        dialog.show();
        reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        databasereal.getReference().child("image")
                                .setValue(uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dialog.dismiss();
                                Toast.makeText(getContext(), "uploaded", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
            }
        });

    }

    private boolean isemailchanged() {
         em = binding.emailBox.getText().toString();
        if(!em.isEmpty())
        {
            database.collection("users")
                    .document(FirebaseAuth.getInstance().getUid())
                    .update("email",em).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    //Toast.makeText(getContext(), "wow", Toast.LENGTH_SHORT).show();
                }
            });
            return true;

        }
        else
        {
            Toast.makeText(getContext(), "Enter email", Toast.LENGTH_SHORT).show();
            return  false;


        }






    }

    private boolean ispasschanged()
    {
        pa = binding.passBox.getText().toString();
        if(!pa.isEmpty())
        {
            database.collection("users")
                    .document(FirebaseAuth.getInstance().getUid())
                    .update("pass",pa).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    //Toast.makeText(getContext(), "hi", Toast.LENGTH_SHORT).show();
                }
            });
            return true;

        }
        else
        {
            Toast.makeText(getContext(), "Enter password", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    private boolean isnamechanged()
    {
        nam = binding.nameBox.getText().toString();
        if(!nam.isEmpty()) {

            database.collection("users")
                    .document(FirebaseAuth.getInstance().getUid())
                    .update("name", nam).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    //Toast.makeText(getContext(), "hi", Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        }
        else {
            Toast.makeText(getContext(), "Enter name", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}