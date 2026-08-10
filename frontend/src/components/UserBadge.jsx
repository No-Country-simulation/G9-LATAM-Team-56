import avatarUser from "../assets/img/avatar-marcela.svg";
import "./UserBadge.css";

function UserBadge({ name = "Marcela G." }) {
  return (
    <div className="user-badge">
      <div className="user-badge-avatar">
        <img src={avatarUser} alt={name} />
      </div>
      <span className="user-badge-name">{name}</span>
    </div>
  );
}

export default UserBadge;
