package com.example.mytravelmap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.LocationTemplate;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class InfoActivity extends FragmentActivity
        implements OnMapReadyCallback {
    private GoogleMap mMap;
    private LatLng latLng;
    private String id;
    private ListViewItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        item = (ListViewItem) getIntent().getSerializableExtra("item");
        boolean visiable = getIntent().getBooleanExtra("visiable", false);

        ImageView imageView = findViewById(R.id.imageView);
        TextView title = findViewById(R.id.textview_info_title);
        TextView content = findViewById(R.id.textview_info_content);
        if (!visiable) {
            Button button = findViewById(R.id.delete);
            button.setVisibility(View.INVISIBLE);
        }

        latLng = item.getLatLng();
        id = item.getId();

        imageView.setImageDrawable(Drawable.createFromPath(item.getImg()));
        title.setText(item.getTitle());
        content.setText(item.getContent());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));
        marker.setTag(id);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
    }

    public void share(View v) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder.setTitle("공유");

        dialogBuilder.setMessage("공유 하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("공유",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                my_share();
                            }
                        })
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    public void delete(View v) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder.setTitle("삭제");

        dialogBuilder.setMessage("삭제 하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("삭제",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                my_delete();
                            }
                        })
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void my_share() {
        Geocoder mGeocoder = new Geocoder(this, Locale.KOREA);

        // 실제 주소 계산
        String address = "";
        try {
            List<Address> mList = mGeocoder.getFromLocation(
                    item.getLatLng().latitude, item.getLatLng().longitude, 1
            );
            address = mList.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 위치 템플릿 생성
        LocationTemplate params = LocationTemplate.newBuilder(address,
                ContentObject.newBuilder(item.getTitle() + "\n" + item.getContent() + "\n",
                        "https://imgur.com/Gzdcdht",
                        LinkObject.newBuilder()
                                .setWebUrl("https://developers.kakao.com")
                                .setMobileWebUrl("https://developers.kakao.com")
                                .build())
                        .setDescrption(address)
                        .build())
                .setAddressTitle(address)
                .build();

        // 콜백 설정
        Map<String, String> serverCallbackArgs = new HashMap<String, String>();
        serverCallbackArgs.put("user_id", "${current_user_id}");
        serverCallbackArgs.put("product_id", "${shared_product_id}");

        // 카카오 링크 전송
        KakaoLinkService.getInstance().sendDefault(this, params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {
            }
        });
    }

    private void my_delete() {
        Intent intent = new Intent();
        intent.putExtra("id", item.getId());
        setResult(RESULT_OK, intent);
        finish();
    }
}
