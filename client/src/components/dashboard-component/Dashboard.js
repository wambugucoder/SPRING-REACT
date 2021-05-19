import "./Dashboard.css"
import { Row, Tabs ,Col,Space} from 'antd';
import ActivePoll from "../active-polls-component/ActivePoll";
import { useSelector } from "react-redux";
import { useHistory } from "react-router";
import { useEffect } from "react";

const { TabPane } = Tabs;


function callback(key) {
  console.log(key);
}

function Dashboard(){
  const auth=useSelector(state=>state.auth);
  const history=useHistory();

  useEffect(() => {
    if(!auth.isAuthenticated){
      history.push("/");
     }
   
  }, [auth.isAuthenticated, history])
    return(
      <div className="polling-bar">
       <div className="polling-tabs">
                <Row justify="center" align="center">
              <Col xs={{ span: 115, offset: 2 }} sm={{ span: 100, offset: 1 }} md={{ span: 25, offset: 1 }} lg={{ span: 18, offset: 2 }}>
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