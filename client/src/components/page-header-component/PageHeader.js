import {
  CalendarOutlined,
  EditOutlined,
  HomeOutlined,
  LogoutOutlined,
  MailOutlined,
  UserOutlined,
} from "@ant-design/icons";
import { Avatar, Image, Menu, PageHeader, Space } from "antd";
import moment from "moment";
import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Link, useHistory } from "react-router-dom";

import RandomColor from "../../constants/RandomColor";
import { LogOutUser } from "../../store/actions/Action";

const { SubMenu } = Menu;

function PollPageHeader() {
  const dispatch = useDispatch();
  const history = useHistory();
  const [current, setCurrent] = useState("home");

  const IconText = ({ icon, text }) => (
    <Space>
      {React.createElement(icon)} {text}
    </Space>
  );

  const HandleClick = (e) => {
    console.log("click ", e);
    setCurrent(e.key);
  };
  const auth = useSelector((state) => state.auth);

  const RenderAvatar = () => {
    if (auth.user.avatar === "none") {
      return (
        <Avatar style={{ backgroundColor: RandomColor() }} shape="circle">
          {auth.user.UserName.charAt(0).toUpperCase()}
        </Avatar>
      );
    } else {
      return <Avatar shape="circle" src={auth.user.Avatar} />;
    }
  };

  const RenderPageHeader = () => {
    if (auth.isAuthenticated === true) {
      const createdAt = moment(auth.user.CreatedAt).format("LLLL");
      return (
        <PageHeader
          style={{ position: "sticky", top: 0, zIndex: 1001 }}
          ghost={false}
          onBack={() => history.goBack()}
          title="Polling App"
          subTitle=""
          extra={[
            <Menu
              onClick={HandleClick}
              selectedKeys={[current]}
              mode="horizontal"
            >
              <Menu.Item key="home" icon={<HomeOutlined />}>
                <Link to="/dashboard">Home</Link>
              </Menu.Item>
              <Menu.Item key="create-poll" icon={<EditOutlined />}>
                <Link to="/create_poll">Create Poll</Link>
              </Menu.Item>
              <SubMenu key="SubMenu" title={<RenderAvatar />}>
                <Menu.ItemGroup
                  style={{
                    fontWeight: "bold",
                  }}
                  title="Profile"
                >
                  <Menu.Item key="setting:1">
                    <IconText icon={UserOutlined} text={auth.user.UserName} />
                  </Menu.Item>
                  <Menu.Item key="setting:2">
                    <IconText icon={MailOutlined} text={auth.user.Email} />
                  </Menu.Item>
                  <Menu.Item key="setting:3">
                    <IconText icon={CalendarOutlined} text={"createdAt"} />
                    <CalendarOutlined />
                    <Space />
                    {createdAt}
                  </Menu.Item>
                </Menu.ItemGroup>
                <Menu.ItemGroup
                  style={{
                    fontWeight: "bold",
                  }}
                  title="Logout"
                >
                  <Menu.Item
                    key="setting:4"
                    onClick={() => {
                      dispatch(LogOutUser());
                    }}
                  >
                    <IconText icon={LogoutOutlined} text={"Exit"} />
                  </Menu.Item>
                </Menu.ItemGroup>
              </SubMenu>
            </Menu>,
          ]}
        ></PageHeader>
      );
    } else {
      return <div></div>;
    }
  };
  return <RenderPageHeader />;
}
export default PollPageHeader;
