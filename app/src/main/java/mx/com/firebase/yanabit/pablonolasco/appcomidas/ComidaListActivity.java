package mx.com.firebase.yanabit.pablonolasco.appcomidas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mx.com.firebase.yanabit.pablonolasco.appcomidas.dummy.DummyContent;

/**
 * An activity representing a list of Comidas. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ComidaDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ComidaListActivity extends AppCompatActivity {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.bt_save)
    Button btSave;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private String PATH = "food";
    private String REFERENCES = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comida_list);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.comida_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.comida_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull final RecyclerView recyclerView) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // TODO: 19/11/18 Firebase se encargará de sincronizar los
        // todo datos cuando haya internet y el maneja su propio control de versiones interno.
        //database.setPersistenceEnabled(true);
        DatabaseReference databaseReference = database.getReference(PATH);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                DummyContent.Comida comida = dataSnapshot.getValue(DummyContent.Comida.class);
                try {
                    if (comida != null) {
                        comida.setId(dataSnapshot.getKey());
                    }
                    // TODO: 19/11/18 Verificamos comida, si no existe, agrega el objeto
                    if (!DummyContent.ITEMS.contains(comida)) {
                        DummyContent.ITEMS.add(comida);
                    }
                    // TODO: 19/11/18 actualizar la vista 
                    recyclerView.getAdapter().notifyDataSetChanged();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                DummyContent.Comida comida = dataSnapshot.getValue(DummyContent.Comida.class);
                try {
                    if (comida != null) {
                        comida.setId(dataSnapshot.getKey());
                    }
                    // TODO: 19/11/18 si encuentra el elemento lo actualiza
                    if (DummyContent.ITEMS.contains(comida)) {
                        DummyContent.update(comida);
                    }
                    // TODO: 19/11/18 actualizar la vista
                    recyclerView.getAdapter().notifyDataSetChanged();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                DummyContent.Comida comida = dataSnapshot.getValue(DummyContent.Comida.class);
                try {
                    if (comida != null) {
                        comida.setId(dataSnapshot.getKey());
                    }
                    // TODO: 19/11/18 si encuentra el elemento lo elimina
                    if (DummyContent.ITEMS.contains(comida)) {
                        DummyContent.delete(comida);
                    }
                    // TODO: 19/11/18 actualizar la vista
                    recyclerView.getAdapter().notifyDataSetChanged();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, mTwoPane));

    }

    @OnClick(R.id.bt_save)
    public void onViewClicked() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // TODO: 19/11/18 Firebase se encargará de sincronizar los
        // todo datos cuando haya internet y el maneja su propio control de versiones interno.
        //database.setPersistenceEnabled(true);
        DatabaseReference databaseReference = database.getReference(PATH);
        DummyContent.Comida comida = new DummyContent.Comida(etName.getText().toString().trim(),
                etPrice.getText().toString().trim());
        // TODO: 19/11/18 Crea la rama dentro del path, en este caso es un nodo de tipo objeto comida
        //todo food/-Comida,-Comida2, etc
        databaseReference.push().setValue(comida);
        etName.setText("");
        etPrice.setText("");
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ComidaListActivity mParentActivity;
        private final List<DummyContent.Comida> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DummyContent.Comida item = (DummyContent.Comida) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ComidaDetailFragment.ARG_ITEM_ID, item.id);
                    ComidaDetailFragment fragment = new ComidaDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.comida_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ComidaDetailActivity.class);
                    intent.putExtra(ComidaDetailFragment.ARG_ITEM_ID, item.id);

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(ComidaListActivity parent,
                                      List<DummyContent.Comida> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.comida_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            String precio = "$" + mValues.get(position).precio;
            holder.mIdView.setText(precio);
            holder.mContentView.setText(mValues.get(position).nombre);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }
}
