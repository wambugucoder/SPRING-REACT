import {useHistory, useParams } from "react-router";
import { Result, Button,Spin, Row, Col } from 'antd';
import "./Activate.css";
import { HourglassTwoTone, InfoCircleTwoTone, SmileTwoTone } from "@ant-design/icons";
import { useDispatch, useSelector } from "react-redux";
import { useEffect } from "react";
import { ActivateUserAccount } from "../../store/actions/Action";
import { useState } from "react";

function Activate(){
    const params=useParams()
    const auth =useSelector(state=>state.auth)
    const error =useSelector(state=>state.error)
    const dispatch=useDispatch()
    const [start,setStart]=useState(0);
    const history=useHistory();
    

useEffect(() => {
  if(start===0){
    setTimeout(() => {
      dispatch(ActivateUserAccount(params.tokenid))
      //console.log("jelo")
    }, 8500);
    setStart(start+1)
  }
  
});

const OnRedirectToHomeClick=()=>{
  history.push("/")
}
const OnRedirectToLoginClick=()=>{
  history.push("/login")
}

const Loader=()=>{
return(
    <Spin className="spinner" size="large" />
);
}

const InCheck=()=>{
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
const Success=()=>{
    return(
        <Result
        status="success"
        title="Account Activation Success"
        subTitle="Your Account Has Successfully Been Activated!"
        extra={[
          <Button onClick={OnRedirectToLoginClick}>
            Proceed To Login
          </Button>,
        ]}
      />
    );
}

const Decider=()=>{

  if(auth.isAccountActivated){
    return(
      <Success/>
    );
  }
  if (error.hasActivationErrors) {
    return(
      <Result
      status="warning"
      title="Account Activation Failed"
      subTitle="Your Token Seems To Be Invalid or Already Activated! "
      extra={[
        <Button  onClick={OnRedirectToHomeClick}>
          Proceed To Home
        </Button>,
       
      ]}
    />
    );
  }
  if(auth.isAccountActivated===false && error.hasActivationErrors===false){
    return(
      <InCheck/>
    );
    }

}
return(
    <Decider/>
)
}
export default Activate;