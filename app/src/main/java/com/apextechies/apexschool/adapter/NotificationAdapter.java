package com.apextechies.apexschool.adapter;

import android.animation.ObjectAnimator;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apextechies.apexschool.R;
import com.apextechies.apexschool.intrface.ClickListener;
import com.apextechies.apexschool.model.NotificationDateList;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Shankar on 1/27/2018.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    ClickListener clickListener;
    int notification_item;
    private List<NotificationDateList> list;
    private SparseBooleanArray expandState = new SparseBooleanArray();
    private Context context;

    public NotificationAdapter(Context context, List<NotificationDateList> list, int notification_item, ClickListener clickListener) {
        this.clickListener = clickListener;
        this.notification_item = notification_item;
        this.list = list;
        for (int i = 0; i < list.size(); i++) {
            expandState.append(i, true);
        }
    }

    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        this.context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(notification_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        viewHolder.setIsRecyclable(false);

        viewHolder.tvName.setText(list.get(i).getNotification_date());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        LinearLayout mll = null;

        LinearLayout ll = null;
        for (int k = 0; k < list.get(i).getNotification().size(); k++) {
            // Create LinearLayout
            mll = new LinearLayout(context);
            mll.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams mllparams = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f);
            mllparams.setMargins(5, 5, 5, 5);

            CircleImageView imageView = new CircleImageView(context);
            Glide.with(context).load("http://www.pngmart.com/files/4/The-Boss-Baby-PNG-Image.png").into(imageView);
            LinearLayout.LayoutParams imageparams = new LinearLayout.LayoutParams(
                    100, 100);
            imageparams.rightMargin = 10;
            imageView.setBorderWidth(2);
            imageView.setLayoutParams(imageparams);
            mll.addView(imageView);

            ll = new LinearLayout(context);
            ll.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 4.0f);
            layoutparams.setMargins(10, 5, 5, 5);
            ll.setLayoutParams(layoutparams);
            // Create TextView
            TextView title = new TextView(context);
            title.setText(list.get(i).getNotification().get(k).getNotification_title());
            title.setTextSize(17);
            title.setMaxLines(1);
            title.setTextColor(Color.parseColor("#212121"));
            ll.addView(title);

            // Create TextView
            TextView schoolName = new TextView(context);
            schoolName.setText(list.get(i).getNotification().get(k).getSchool_id());
            schoolName.setTextSize(15);
            schoolName.setTextColor(Color.parseColor("#212121"));
            schoolName.setMaxLines(1);
            ll.addView(schoolName);

            TextView description = new TextView(context);
            description.setText(list.get(i).getNotification().get(k).getNotification_description());
            description.setMaxLines(1);
            description.setTextColor(Color.parseColor("#757575"));
            description.setTextSize(13);
            ll.addView(description);


            if (list.get(i).getNotification().get(k).getNotification_image().size() > 0) {
                HorizontalScrollView horizontalScrollView = new HorizontalScrollView(context);
                horizontalScrollView.setHorizontalScrollBarEnabled(false);
                horizontalScrollView.setHorizontalFadingEdgeEnabled(false);
                LinearLayout.LayoutParams scrollparams = new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                scrollparams.setMargins(0, 10, 0, 0);
                horizontalScrollView.setLayoutParams(scrollparams);
                LinearLayout linearLayout = new LinearLayout(context);
                LinearLayout.LayoutParams llparams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                llparams.setMargins(5, 5, 5, 0);
                linearLayout.setLayoutParams(llparams);
                for (int j = 0; j < list.get(i).getNotification().get(k).getNotification_image().size(); j++) {
                    ImageView imageView1 = new ImageView(context);
                    imageView1.setScaleType(ImageView.ScaleType.FIT_XY);
                    LinearLayout.LayoutParams imagescrollparams = new LinearLayout.LayoutParams(
                            250, 250);
                    imagescrollparams.setMargins(0, 5, 10, 0);
                    // Glide.with(context).load(list.get(j).getNotification_image().get(k).getNotification_image()).into(imageView);
                    Glide.with(context).load(list.get(i).getNotification().get(k).getNotification_image().get(j).getNotification_image()).into(imageView1);
                    imageView1.setLayoutParams(imagescrollparams);
                    linearLayout.addView(imageView1);
                }
                try {
                    horizontalScrollView.addView(linearLayout);
                    ll.addView(horizontalScrollView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            View view = new View(context);
            view.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            view.setMinimumHeight(1);
            LinearLayout.LayoutParams viewparams = new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            viewparams.setMargins(0, 10, 0, 10);
            view.setLayoutParams(viewparams);
            ll.addView(view);


            mll.addView(ll);
            //Add button to LinearLayout defined in XML
            viewHolder.expandableLayout.addView(mll);
        }


        //check if view is expanded
        final boolean isExpanded = expandState.get(i);
        viewHolder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        viewHolder.buttonLayout.setRotation(expandState.get(i) ? 180f : 0f);
        viewHolder.buttonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onClickButton(viewHolder.expandableLayout, viewHolder.buttonLayout, i);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    private void onClickButton(final LinearLayout expandableLayout, final RelativeLayout buttonLayout, final int i) {

        //Simply set View to Gone if not expanded
        //Not necessary but I put simple rotation on button layout
        if (expandableLayout.getVisibility() == View.VISIBLE) {
            createRotateAnimator(buttonLayout, 180f, 0f).start();
            expandableLayout.setVisibility(View.GONE);
            expandState.put(i, false);
        } else {
            createRotateAnimator(buttonLayout, 0f, 180f).start();
            expandableLayout.setVisibility(View.VISIBLE);
            expandState.put(i, true);
        }
    }

    //Code to rotate button
    private ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textView_name)
        TextView tvName;
        @BindView(R.id.button)
        RelativeLayout buttonLayout;
        @BindView(R.id.expandableLayout)
        LinearLayout expandableLayout;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
