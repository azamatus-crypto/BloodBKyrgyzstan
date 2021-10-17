package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.changeyourself.KGBlood.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pojo.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
   private List<User>userList;
   private Context context;

    public UserAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_displayed,parent,false);
        return new UserViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user=userList.get(position);
        holder.username.setText(user.getName());
        if(user.getType().equals("Донор")){
            holder.btemailnow.setVisibility(View.GONE);
        }
        holder.useremail.setText("Почтa: "+user.getEmail());
        holder.phoneNumber.setText("Hомер телефона: "+user.getPhonenumber());
        holder.bloodgroup.setText("Группa крови: "+user.getBloodgroup());
        holder.age.setText("Bозраст: "+user.getAge());
        holder.type.setText(user.getType());
        Glide.with(context).load(user.getProfilepictureurl()).into(holder.circleImageView);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView circleImageView;
        private TextView type;
        private TextView username;
        private TextView useremail;
        private TextView phoneNumber;
        private TextView bloodgroup;
        private TextView age;
        private Button btemailnow;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.userProfileImage);
            type=itemView.findViewById(R.id.typeuserikadap);
            username=itemView.findViewById(R.id.userName);
            useremail=itemView.findViewById(R.id.userEmail);
            phoneNumber=itemView.findViewById(R.id.phoneNumber);
            bloodgroup=itemView.findViewById(R.id.bloodGroup);
            age=itemView.findViewById(R.id.ageuserdisplayed);
            btemailnow=itemView.findViewById(R.id.emailNow);
        }
    }
}
