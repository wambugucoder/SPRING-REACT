import Title from "antd/lib/typography/Title"
import { Link } from "react-router-dom"
import "./Dashboard.css"
import { Row, Tabs ,Col,Space,PageHeader, Menu,Avatar,Image } from 'antd';

import { MailOutlined, AppstoreOutlined,HomeOutlined,EditOutlined, UserOutlined,CalendarOutlined, LogoutOutlined } from '@ant-design/icons';
import { useState } from "react";
import PollPageHeader from "../page-header-component/PageHeader";
import ActivePoll from "../active-polls-component/ActivePoll";

const { TabPane } = Tabs;
const { SubMenu } = Menu;

function callback(key) {
  console.log(key);
}

function Dashboard(){

  const[current,setCurrent]=useState("home")

  const HandleClick = e => {
    console.log('click ', e);
    setCurrent(e.key);
  };
  
    return(
      <div className="polling-bar">
       <div className="polling-tabs">
                <Row justify="center" align="center">
              <Col xs={{ span: 105, offset: 2 }} sm={{ span: 100, offset: 1 }} md={{ span: 95, offset: 1 }} lg={{ span: 18, offset: 2 }}>
              <Tabs defaultActiveKey="1" onChange={callback}>
    <TabPane tab="Active Polls" key="1">
      <div className="polling-section-1"><ActivePoll/></div>
    </TabPane>
    <Space/>
    <TabPane tab="Scheduled Polls" key="2">
      Content of Tab Pane 2
    </TabPane>
    <Space/>
    <TabPane tab="Closed Polls" key="3">
      Content of Tab Pane 3
    </TabPane>
  </Tabs>
              </Col>
                </Row>
    
            </div>
      </div>

    );

}
export default Dashboard;