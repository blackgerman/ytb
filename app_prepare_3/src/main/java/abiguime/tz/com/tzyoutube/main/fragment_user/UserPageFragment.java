package abiguime.tz.com.tzyoutube.main.fragment_user;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import abiguime.tz.com.tzyoutube.R;
import abiguime.tz.com.tzyoutube._data.User;
import abiguime.tz.com.tzyoutube.main.fragment_home.HomePageFragment;


public class UserPageFragment extends Fragment implements UserPageContract.View {

    private OnFragmentInteractionListener mListener;
    private UserPagePresenter presenter;


    public UserPageFragment() {
        // Required empty public constructor
    }

    public static UserPageFragment newInstance() {
        UserPageFragment fragment = new UserPageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_userpage, container, false);
    }


    EditText ed_username, ed_password;
    Button bt;
    TextView message;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ed_password = (EditText) view.findViewById(R.id.password);
        ed_username = (EditText) view.findViewById(R.id.username);
        bt = (Button) view.findViewById(R.id.bt_login);

        message = (TextView) view.findViewById(R.id.textview);

        /// 登录
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.login("", "");
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void showUser(final User user) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                message.setText(user.name);
                message.setTextColor(Color.GREEN);
            }
        });
    }

    @Override
    public void onLoginProgress() {
        // 显示与正在登录有关的进度条
    }

    @Override
    public void onLoginError(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UserPageFragment.this.message.setText(message);
                UserPageFragment.this.message.setTextColor(Color.RED);
            }
        });
    }


    @Override
    public void setPresenter(UserPageContract.Presenter presenter) {
        this.presenter = (UserPagePresenter) presenter;
    }

    public static UserPageFragment newinstance() {
        return new UserPageFragment();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
