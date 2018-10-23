package vn.ifactory.retrofit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import vn.ifactory.retrofit.R;
import vn.ifactory.retrofit.model.ToDo;

/**
 * Created by PC on 10/23/2018.
 */

public class TodoItemAdapter extends RecyclerView.Adapter<TodoItemAdapter.MyHolder> {

    Context mContext;
    private List<ToDo> listTodo;
    IOnItemListener mListener;

    public interface IOnItemListener {
        void onItemClick(int pos);
        void onDeleteTodo(int pos);
    }

    public void setItemListener (IOnItemListener listener) {
        mListener = listener;
    }
    public TodoItemAdapter(Context context, List<ToDo> listTodo) {
        mContext = context;
        this.listTodo = listTodo;
    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.custom_item_todo, parent, false);

        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        ToDo toDo = listTodo.get(position);
        holder.txtTodoName.setText(toDo.getName());
        holder.txtCreateDate.setText(toDo.getCreateDate());
        holder.txtDescription.setText(toDo.getDescription());

    }

    @Override
    public int getItemCount() {
        return listTodo != null ? listTodo.size() : 0;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txtTodoName;
        TextView txtCreateDate;
        TextView txtDescription;

        public MyHolder(View view) {
            super(view);
            this.txtTodoName = view.findViewById(R.id.txtToDoName);
            this.txtCreateDate = view.findViewById(R.id.txtCreateDate);
            this.txtDescription = view.findViewById(R.id.txtDescription);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(mContext, "Item click " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    if (mListener != null) {
                        mListener.onItemClick(getAdapterPosition());
                    }
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mListener != null) {
                        mListener.onDeleteTodo(getAdapterPosition());
                    }
                    return true;
                }
            });
        }
    }
}
