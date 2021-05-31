import "./Landing.css";

import {GithubOutlined, LineChartOutlined} from '@ant-design/icons';
import {Button, Col, Row, Typography} from 'antd';
import {useEffect} from 'react';
import {useSelector} from 'react-redux';
import {Link, useHistory} from "react-router-dom";

function Landing() {

  const auth = useSelector(state => state.auth);
  const {Title} = Typography;
  const history = useHistory();

  useEffect(() => {
    if (auth.isAuthenticated) {
      history.push("/dashboard");
    }
  }, [ auth.isAuthenticated, history ])

  // BUTTON FUNCTIONALITY
    const OnLoginClick=()=> {
    history.push("/login")
    }
        
    const OnRegisterClick=()=> {
    history.push("/register")
    }
        
    const RenderLoginButton=()=> {
        return(
            <Button className="landing-buttons" type="primary" block
            onClick={OnLoginClick}>Login</Button>
            )
    }
    const RenderSignUpButton=()=> {
        return(
        <Button className="landing-buttons" type="danger" block
        onClick={OnRegisterClick}>SignUp</Button>
               
        )
        }
    
    return (
  <div className="container-content" >            
    <div className="content-topic">
     <Row  justify="center" align="middle">
     <Col xs={
    { span: 24, offset: 1 }} sm={
    { span: 10, offset: 1 }} md={
    { span: 8, offset: 1 }} lg={{
    span: 6, offset: 1 }}>
      <Title level={1}><LineChartOutlined/> Polling App</Title>
     </Col>
     </Row>
     </div>
     <div className="content-subtopic">
     <Row  justify="center" align="middle">
     <Col xs={
    { span: 100.5, offset: 1 }} sm={
    { span: 16, offset: 1 }} md={
    { span: 10, offset: 1 }} lg={{
    span: 6, offset: 1 }}>
      <Title className="typewriter"  level={5}>Vote With Ease From Your Home...</Title>
     </Col>
     </Row>
     </div>
     <div className="content-buttons">
   <Row justify="center" align="middle">
   <Col xs={
    { span: 10, offset: 0 }} lg={{
    span: 4, offset: 2 }}><RenderLoginButton/></Col>
   <Col xs={{ span: 10, offset: 1 }} lg={{ span: 4, offset: 3 }}><RenderSignUpButton/></Col>
   
     </Row>
     </div>

     <div className="footer">
         <Row justify="center" align="middle">
         <Col xs={{ span: 20.5, offset: 0 }} lg={{ span: 3, offset: 2}}>
             <Link  to ="/privacy-policy" className="link"><p>Privacy Policy</p></Link></Col>
         <Col xs={
    { span: 20.5, offset: 1 }} lg={{
    span: 3, offset: 2.5}}><Link to ="/issues" className="link"><p>Report A Bug</p></Link></Col>
         <Col xs={{ span: 20.5, offset: 3 }} lg={{ span: 2.3, offset: 2.6}}><Link to ="/project" className="link"><p><GithubOutlined style={{fontSize:20}}/></p></Link></Col>
        
         </Row>
     
     </div>
     </div>
      
        
        
          
        
       
      
       
    )
}
export default Landing;