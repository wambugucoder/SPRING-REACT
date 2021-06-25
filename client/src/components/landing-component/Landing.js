import {Typography,Button,Row, Col, Space } from 'antd';
import {LineChartOutlined,GithubOutlined } from '@ant-design/icons';
import { useThemeSwitcher } from "react-css-theme-switcher";
import{Link,useHistory}from "react-router-dom";
import "./Landing.css";
import { useSelector } from 'react-redux';
import { useEffect } from 'react';


function Landing() {

    const auth=useSelector(state=>state.auth);
    const { Title } = Typography;
    const { currentTheme } = useThemeSwitcher();
    const history=useHistory();
    
    useEffect(() => {
      if(auth.isAuthenticated){
        history.push("/dashboard");
       }
     
    }, [auth.isAuthenticated, history])

   


    //BUTTON FUNCTIONALITY
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
     <Col xs={{ span: 24, offset: 1 }} sm={{ span: 10, offset: 1 }} md={{ span: 8, offset: 1 }} lg={{ span: 6, offset: 1 }}>
      <Title level={1}><LineChartOutlined/> Polling App</Title>
     </Col>
     </Row>
     </div>
     <div className="content-subtopic">
     <Row  justify="center" align="middle">
     <Col xs={{ span: 100.5, offset: 1 }} sm={{ span: 16, offset: 1 }} md={{ span: 10, offset: 1 }} lg={{ span: 6, offset: 1 }}>
      <Title className="typewriter"  level={5}>Vote With Ease From Your Home...</Title>
     </Col>
     </Row>
     </div>
     <div className="content-buttons">
   <Row justify="center" align="middle">
   <Col xs={{ span: 10, offset: 0 }} lg={{ span: 4, offset: 2 }}><RenderLoginButton/></Col>
   <Col xs={{ span: 10, offset: 1 }} lg={{ span: 4, offset: 3 }}><RenderSignUpButton/></Col>
   
     </Row>
     </div>

     <div className="footer">
         <Row justify="center" align="middle">
         <Col xs={{ span: 19, offset: 0 }} lg={{ span: 18, offset: 0}}>
             <span className="footer-notes"> <Link  to ="/privacy-policy" className={currentTheme==="dark"?"link":""}><p>Privacy Policy</p></Link></span>
             <span className="footer-notes"> </span>
             <span className="footer-notes"> <Link to ="/issues" className={currentTheme==="dark"?"link":""}><p>Report A Bug</p></Link></span>
             <span className="footer-notes"> </span>
             <span className="footer-notes">  <Link to ="/project" className={currentTheme==="dark"?"link":""}><GithubOutlined style={{fontSize:20}}/></Link>
             </span>
             
             
            </Col>
         

        
         </Row>
     
     </div>
     </div>
      
        
        
          
        
       
      
       
    )
}
export default Landing;