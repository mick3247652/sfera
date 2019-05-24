package ru.club.sfera.common;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import ru.club.sfera.ProfileFragment;
import ru.club.sfera.R;
import ru.club.sfera.constants.Constants;

public class FragmentBase extends Fragment implements Constants {

    private String className = ProfileFragment.class.getSimpleName();

    private ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        initpDialog();
    }

    protected void initpDialog() {

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.msg_loading));
        pDialog.setCancelable(false);
    }

    protected void showpDialog() {

        if (!pDialog.isShowing()) {

            try {

                pDialog.show();

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    protected void hidepDialog() {

        if (pDialog.isShowing()) {

            try {

                pDialog.dismiss();

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    public void setClassName(String className) {

        this.className = className;
    }

    public String getClassName() {

        return this.className;
    }
}
