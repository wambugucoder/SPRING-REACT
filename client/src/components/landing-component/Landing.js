import {PageHeader, Button,Card} from 'antd';
import{useHistory}from "react-router-dom";
import "./Landing.css";
import { useSelector } from 'react-redux';
import { useEffect } from 'react';
import Theme from '../theme-switch/Theme';
import GoogleButton from 'react-google-button';
import { GITHUB_AUTH_URL, GOOGLE_AUTH_URL } from '../../constants/Constant';
import GithubButton from 'react-github-login-button';





function Landing() {
    const auth=useSelector(state=>state.auth);
    const history=useHistory();
    
    useEffect(() => {
      if(auth.isAuthenticated){
        history.push("/dashboard");
       }
     
    }, [auth.isAuthenticated, history])

   


    //BUTTON FUNCTIONALITY
   
    const OnGithubClick=()=>{
        window.location.href=GITHUB_AUTH_URL
      }
      const OnGoogleClick=()=>{
        window.location.href=GOOGLE_AUTH_URL
      }  
   
        
  const Land=()=>{
      return(
          <div>
             
              <PageHeader
              backIcon={false}
               ghost={false}
               onBack={() => window.history.back()}
               title="JKUAT POLLS"
              subTitle={<Theme />}
             extra={[
             <Button key="3"onClick={()=>{history.push("/login")}}>Login</Button>,
             <Button key="2" onClick={()=>{history.push("/register")}}>Register</Button>,
             
        
      ]}
   />
   
   <div class="outer">
  <div class="img">
    
  <img className="example-link-icon" src='https://img.freepik.com/free-vector/democracy-online-vote-vector-illustration-concept-people-give-vote-putting-paper-vote-ballot-box_126608-402.jpg?size=650&ext=jpg' alt="vote"/> 
  </div>
  <div class="txt">
      <div className="content-topic">
      Taking School Elections To The 🚀
      </div>
      <div className="content-subtopic">
      <p class="line-1 anim-typewriter">Voting Done Easy </p>
      </div>
        <Card className="oauth-card">
        <div className="oauth">
     <GoogleButton
          style={{marginLeft:"10%"}}
            type="dark"
            onClick={OnGoogleClick}
            />
          
        
      </div>
      <div className="oauth">
      <GithubButton
      style={{marginLeft:"10%"}}
            type="dark"
            onClick={OnGithubClick} 
           />
      </div>
        </Card>
     

    
  </div>
</div>   
   </div>
    
      )
  }     
    return (
 <div className="container-content">
   <ul class="circles">
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
            </ul> 
<Land/>

     
 </div>
      
        
        
          
        
       
      
       
    )
}
export default Landing;