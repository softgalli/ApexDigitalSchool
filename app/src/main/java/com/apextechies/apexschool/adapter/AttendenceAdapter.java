package com.apextechies.apexschool.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apextechies.apexschool.R;
import com.apextechies.apexschool.model.Student;

import java.util.List;

public class AttendenceAdapter extends
        RecyclerView.Adapter<AttendenceAdapter.ViewHolder> {

    private List<Student> mStudentsList;
    private Activity mActivity;

    public AttendenceAdapter(List<Student> students, Activity mActivity) {
        this.mStudentsList = students;
        this.mActivity = mActivity;
    }

    // Create new views
    @Override
    public AttendenceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_itemview, null);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        final int pos = position;
        if (mStudentsList != null && mStudentsList.size() > 0) {
            viewHolder.tvName.setText(mStudentsList.get(position).getStudentName());

            viewHolder.tvEmailId.setText(mStudentsList.get(position).getStudentId());

            viewHolder.presentButton.setTag(mStudentsList.get(position));
            viewHolder.absentButton.setTag(mStudentsList.get(position));


            viewHolder.presentButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    managePresentAbsent(viewHolder.presentButton, viewHolder.absentButton, true);
                    Button cb = (Button) v;
                    Student contact = (Student) cb.getTag();
                    contact.setSelected(true);
                    mStudentsList.get(pos).setSelected(true);

                    Toast.makeText(v.getContext(), cb.getText() + " is present", Toast.LENGTH_LONG).show();
                }
            });
            viewHolder.absentButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    managePresentAbsent(viewHolder.presentButton, viewHolder.absentButton, false);
                    Button cb = (Button) v;
                    Student contact = (Student) cb.getTag();

                    contact.setSelected(false);
                    mStudentsList.get(pos).setSelected(false);

                    Toast.makeText(v.getContext(), cb.getText() + " is absent", Toast.LENGTH_LONG).show();
                }
            });

            if (mStudentsList.get(position).isSelected()) {
                managePresentAbsent(viewHolder.presentButton, viewHolder.absentButton, true);
            } else {
                managePresentAbsent(viewHolder.absentButton, viewHolder.presentButton, false);
            }
        }
    }

    private void managePresentAbsent(Button presentBtn, Button absentBtn, boolean isPresent) {
        if (presentBtn != null && absentBtn != null) {
            if (isPresent) {
                presentBtn.setTextColor(mActivity.getResources().getColor(R.color.colorTextWhite));
                presentBtn.setBackground(mActivity.getResources().getDrawable(R.drawable.present_checkbox_image));
                absentBtn.setTextColor(mActivity.getResources().getColor(R.color.textColor));
                absentBtn.setBackground(mActivity.getResources().getDrawable(R.drawable.border));
            } else {
                presentBtn.setTextColor(mActivity.getResources().getColor(R.color.textColor));
                presentBtn.setBackground(mActivity.getResources().getDrawable(R.drawable.border));
                absentBtn.setTextColor(mActivity.getResources().getColor(R.color.colorTextWhite));
                absentBtn.setBackground(mActivity.getResources().getDrawable(R.drawable.absent_checkbox_image));
            }
        }
    }

    // Return the size arraylist
    @Override
    public int getItemCount() {
        return mStudentsList.size();
    }

    // method to access in activity after updating selection
    public List<Student> getStudentist() {
        return mStudentsList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvEmailId;
        public Button absentButton, presentButton;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tvName = itemLayoutView.findViewById(R.id.tvName);
            tvEmailId = itemLayoutView.findViewById(R.id.tvEmailId);
            absentButton = itemLayoutView.findViewById(R.id.absentButton);
            presentButton = itemLayoutView.findViewById(R.id.presentButton);
        }

    }

}
