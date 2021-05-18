import { CalendarFilled, FormOutlined } from '@ant-design/icons';
import { Row, Tabs ,Col,Space} from 'antd';
import NonScheduledPoll from '../non-scheduled-poll-component/NonScheduledPoll';
import ScheduledPoll from '../scheduled-poll-container/ScheduledPoll';
import "./CreatePollDashboard.css";
const { TabPane } = Tabs;

function callback(key) {
    console.log(key);
  }
  
function CreatePollDashboard(){
    return(
        <div className="poll-activity">
       <div className="poll-menu">
                <Row justify="center" align="center">
              <Col xs={{ span: 105, offset: 2 }} sm={{ span: 100, offset: 1 }} md={{ span: 95, offset: 1 }} lg={{ span: 10.5, offset: 2 }}>
              <Tabs defaultActiveKey="1" onChange={callback}>
    <TabPane tab={<span><FormOutlined/>Create Poll</span>} key="1">
      <div className="poll-section-1"><NonScheduledPoll/></div>
    </TabPane>
    <Space/>
    <TabPane tab={<span><CalendarFilled/>Schedule Poll</span>} key="2">
    <div className="poll-section-1"><ScheduledPoll/></div>
    </TabPane>
  </Tabs>
              </Col>
                </Row>
    
            </div>
      </div>
    );

}
export default CreatePollDashboard;