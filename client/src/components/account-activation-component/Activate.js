import { useParams } from "react-router";
import { Result, Button,Spin, Row, Col } from 'antd';
import "./Activate.css";
import { HourglassTwoTone, InfoCircleTwoTone, SmileTwoTone } from "@ant-design/icons";

function Activate(){
    const params=useParams()

const Loader=()=>{
return(
    <Spin className="spinner" size="large" />
);
}
const success=()=>{
    return(
        <Result
        status="success"
        title="Successfully Purchased Cloud Server ECS!"
        subTitle="Order number: 2017182818828182881 Cloud server configuration takes 1-5 minutes, please wait."
        extra={[
          <Button type="primary" key="console">
            Go Console
          </Button>,
          <Button key="buy">Buy Again</Button>,
        ]}
      />
    );
}
return(
    <div className="activate-account">
        <div className="activate-content">
        <Row justify="center" align="middle">
     <Col xs={{ span:6, offset: 4 }} sm={{ span: 6, offset: 4 }} md={{ span: 6, offset: 4 }} lg={{ span: 6, offset: 5 }}>
     <Loader/>
     </Col>
     </Row>
        </div>
        <div class="codinfox-changing-keywords" id="change">
        <Row justify="center" align="middle">
     <Col xs={{ span:17, offset: 1 }} sm={{ span: 16, offset: 4 }} md={{ span: 15, offset: 4 }} lg={{ span: 10, offset: 1 }}>
     <strong>
            <b class="hidden">Hey There User,Welcome To The Polling App <SmileTwoTone/></b><br/>
            <b class="hidden">Getting Ready To Activate your Account <HourglassTwoTone/></b><br/>
            <b class="hidden">Validating Your Token Credentials...<InfoCircleTwoTone/></b><br/>
            <b class="hidden">We're Almost Done,Just One More Thing...</b>
        </strong>   
     </Col>
     </Row>
     </div>
  
    </div>
);
}
export default Activate;