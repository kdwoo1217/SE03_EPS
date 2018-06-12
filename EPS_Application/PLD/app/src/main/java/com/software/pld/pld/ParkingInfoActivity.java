package com.software.pld.pld;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.kakao.kakaonavi.KakaoNaviParams;
import com.kakao.kakaonavi.KakaoNaviService;
import com.kakao.kakaonavi.Location;
import com.kakao.kakaonavi.NaviOptions;
import com.kakao.kakaonavi.options.CoordType;

import java.util.ArrayList;
import java.util.List;

public class ParkingInfoActivity extends AppCompatActivity {
    private String name, note;
    private double lat, lng;
    private boolean isFree;
    private int total, current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = getIntent().getStringExtra("Name");
        lat = getIntent().getDoubleExtra("Latitude", 0);
        lng = getIntent().getDoubleExtra("Longitude", 0);
        note = getIntent().getStringExtra("Note");
        isFree = getIntent().getBooleanExtra("isFree", true);
        total = getIntent().getIntExtra("Total", 0);
        current = getIntent().getIntExtra("Current", 0);

        StringBuilder builder = new StringBuilder();
        builder.append("\n\n");
        builder.append("주차장 이름 : ");
        builder.append(name);
        builder.append("\n\n");
        builder.append("Latitude : ");
        builder.append(lat);
        builder.append("\n\n");
        builder.append("Longitude : ");
        builder.append(lng);
        builder.append("\n\n");
        builder.append("총 차량 수 : ");
        builder.append(total);
        builder.append("\n\n");
        builder.append("현재 차량 수 : ");
        builder.append(current);
        builder.append("\n\n");
        builder.append("무료 여부 : ");
        builder.append(isFree);
        builder.append("\n\n");
        builder.append("메모 : ");
        builder.append(note);
        builder.append("\n\n");

        TextView textView = (TextView) findViewById(R.id.info_text);
        textView.setText(builder.toString());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 경유지 설정
                List<Location> viaList = new ArrayList<Location>();

                final KakaoNaviParams.Builder builder =
                        KakaoNaviParams.newBuilder(Location.newBuilder(name, (float)lng, (float)lat).build())
                                .setNaviOptions(NaviOptions.newBuilder().setCoordType(CoordType.WGS84)
                                .setStartX(126.5).setStartY(35.2).build()).setViaList(viaList);

                KakaoNaviService.getInstance().navigate(ParkingInfoActivity.this, builder.build());
            }
        });

        /*
        naviFab = (FloatingActionButton) findViewById(R.id.fab);
        naviFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 경유지 설정
                List<Location> viaList = new ArrayList<Location>();

                final KakaoNaviParams.Builder builder =
                        KakaoNaviParams.newBuilder(Location.newBuilder(getIntent().getStringExtra("Name"),
                                getIntent().getDoubleExtra("Latitude", 0), getIntent().getDoubleExtra("Longitude", 0))
                                .build()).setNaviOptions(NaviOptions.newBuilder().setCoordType(CoordType.WGS84)
                                .setStartX(126.5).setStartY(35.2).build()).setViaList(viaList);

                KakaoNaviService.getInstance().navigate(ParkingInfoActivity.this, builder.build());
            }
        });
        */
    }

}
