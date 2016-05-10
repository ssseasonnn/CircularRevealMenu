package app.season.expandableactionbutton;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.fab)
    FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    public void onClick() {
        MenuFragment menuFragment = new MenuFragment();
        menuFragment.setOnAnimatorExcute(new MenuFragment.OnAnimatorExcute() {
            @Override
            public void onEnter() {
                mFab.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onExit() {
                mFab.setVisibility(View.VISIBLE);
            }
        });
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, menuFragment, "fragment_my")
                .addToBackStack("fragment:reveal")
                .commit();
    }

    @Override
    public void onBackPressed() {
        MenuFragment fragment = (MenuFragment) getSupportFragmentManager().findFragmentByTag("fragment_my");
        if (fragment != null) {
            fragment.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    public void showFab() {
        mFab.setVisibility(View.VISIBLE);
    }

    public void hideFab() {
        mFab.setVisibility(View.INVISIBLE);
    }
}
