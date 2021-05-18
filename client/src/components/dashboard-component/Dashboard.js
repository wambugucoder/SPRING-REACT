import "./Dashboard.css"
import { Row, Tabs ,Col,Space} from 'antd';
import ActivePoll from "../active-polls-component/ActivePoll";

const { TabPane } = Tabs;


function callback(key) {
  console.log(key);
}

function Dashboard(){
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