import React from "react";
import emailIcon from "./icon/email.png";
import addressIcon from "./icon/address-icon.png";
import dobIcon from "./icon/dob-icon.png";
import genderIcon from "./icon/gender-icon.png";
import phoneIcon from "./icon/phone-icon.png";
const Profile = ({ userInfo }) => {
  return (
    <div>
      <div className="main">
        <div className="left-profile">
          <div className="img-profile">
            <img
              src={`http://localhost:8888/api/user/image/${userInfo.urlProfilePicture}`}
              alt="Profile"
              onError={(e) => {
                e.target.src = "https://via.placeholder.com/150"; // Hình ảnh placeholder nếu không tìm thấy ảnh
              }}
              className="profile-img"
            />
          </div>
          <p className="name-user">
            {userInfo.lastName} {userInfo.firstName}
          </p>
          <div className="style-flex">
            <img className="icon" src={emailIcon} alt="" />
            <p className="email-user"> {userInfo.email}</p>
          </div>
          <div className="underline-user" />
          <div className="info-user">
            <div className="style-flex">
              <img className="icon" src={phoneIcon} alt="" />
              <p className="email-user"> 0345695203</p>
            </div>
            <div className="style-flex">
              <img className="icon" src={addressIcon} alt="" />
              <p className="email-user"> {userInfo.address}</p>
            </div>
            <div className="style-flex">
              <img className="icon" src={dobIcon} alt="" />
              <p className="email-user"> {userInfo.dob}</p>
            </div>
            <div className="style-flex">
              <img className="icon" src={genderIcon} alt="" />
              <p className="email-user"> {userInfo.gender}</p>
            </div>
          </div>
          <div className="underline-user" />
        </div>

        <div className="center">
          <div className="my_post"></div>
        </div>
      </div>
    </div>
  );
};
export default Profile;
