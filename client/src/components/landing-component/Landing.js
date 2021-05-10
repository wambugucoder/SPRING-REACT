import {Typography,Button,Row, Col } from 'antd';
import {LineChartOutlined } from '@ant-design/icons';
import "./Landing.css";
import Snowfall from 'react-snowfall';



const { Title } = Typography;


function RenderLoginButton() {
    return(
      
        <Button className="landing-buttons" type="primary" block >Login</Button>
       
       )
}
function RenderSignUpButton() {
    return(
        <Button className="landing-buttons" type="danger" block>SignUp</Button>
       
       )
}
 function Landing() {
    return (
        <div className="container">
            <div className="App-header">
            <Snowfall />
                
            </div>
     <div className="content-topic">
     <Row  justify="center" align="middle">
     <Col xs={{ span: 14, offset: 1 }} sm={{ span: 10, offset: 1 }} md={{ span: 8, offset: 1 }} lg={{ span: 6, offset: 2 }}>
      <Title level={1}><LineChartOutlined/> Polling App</Title>
     </Col>
     </Row>
     </div>
     <div className="content-subtopic">
     <Row  justify="center" align="middle">
     <Col xs={{ span: 18, offset: 1 }} sm={{ span: 16, offset: 1 }} md={{ span: 10, offset: 1 }} lg={{ span: 6, offset: 2 }}>
      <Title className="typewriter"  level={5}>Vote With Ease From Your Home...</Title>
     </Col>
     </Row>
     </div>
     <div className="content-buttons">
   <Row justify="center" align="middle">
   <Col xs={{ span: 6, offset: 1 }} lg={{ span: 6, offset: 2 }}><RenderLoginButton/></Col>
   <Col xs={{ span: 6, offset: 1 }} lg={{ span: 6, offset: 3 }}><RenderSignUpButton/></Col>
   
     </Row>
     </div>
    

        </div>
      
        
        
          
        
       
      
       
    )
}
export default Landing;