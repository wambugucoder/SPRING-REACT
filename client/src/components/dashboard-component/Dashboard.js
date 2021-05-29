import "./Dashboard.css"
import { Row, Tabs ,Col,Space, message} from 'antd';
import ActivePoll from "../active-polls-component/ActivePoll";
import { useDispatch, useSelector } from "react-redux";
import { useHistory } from "react-router";
import { useEffect } from "react";
import { CleanupPoll } from "../../store/actions/Action";
import ScheduledList from "../Scheduled-Poll-list/ScheduledList";
import ClosedPollList from "../closed-poll-list/ClosedPollList";

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
              <Col xs={{ span: 25, offset:1 }} sm={{ span: 100, offset: 1 }} md={{ span: 25, offset: 1 }} lg={{ span: 18, offset: 2 }}>
              <Tabs defaultActiveKey="1" onChange={callback}>
    <TabPane tab="Active Polls" key="1">
      <div className="polling-section-1"><ActivePoll/></div>
    </TabPane>
    <Space/>
    <TabPane tab="Scheduled Polls" key="2">
    <div className="polling-section-1"><ScheduledList/></div>
    </TabPane>
    <Space/>
    <TabPane tab="Closed Polls" key="3">
    <div className="polling-section-1"><ClosedPollList/></div>
    </TabPane>
  </Tabs>
              </Col>
                </Row>
    
            </div>
      </div>

    );

}
export default Dashboard;