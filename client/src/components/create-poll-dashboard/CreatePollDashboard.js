import "./CreatePollDashboard.css";

import { CalendarFilled, FormOutlined } from "@ant-design/icons";
import { Col, message, notification, Row, Space, Tabs } from "antd";
import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useHistory } from "react-router";

import { CleanupErrors, CleanupPoll } from "../../store/actions/Action";
import NonScheduledPoll from "../non-scheduled-poll-component/NonScheduledPoll";
import ScheduledPoll from "../scheduled-poll-container/ScheduledPoll";

const { TabPane } = Tabs;

function callback(key) {
  console.log(key);
}

function CreatePollDashboard() {
  const dispatch = useDispatch();
  const auth = useSelector((state) => state.auth);
  const poll = useSelector((state) => state.poll);
  const error = useSelector((state) => state.error);
  const history = useHistory();

  useEffect(() => {
    if (auth.isAuthenticated === false) {
      history.push("/login");
    }
  });

  useEffect(() => {
    if (poll.isLoading === true && error.isLoading === true) {
      message.loading("Creating Poll..", 0.9);
    }
  }, [poll.isLoading, error.isLoading]);

  useEffect(() => {
    if (
      poll.hasCreatedScheduledPoll === true ||
      poll.hasCreatedNonScheduledPoll === true
    ) {
      message.success("Poll has Been Created Successfully");
    }
    return () => {
      dispatch(CleanupPoll());
    };
  }, [
    dispatch,
    poll.hasCreatedNonScheduledPoll,
    poll.hasCreatedScheduledPoll,
    poll.isLoading,
  ]);

  useEffect(() => {
    if (error.hasScheduledPollErrors || error.hasNonScheduledPollErrors) {
      notification.error({
        message: "Poll Error",
        description: error.errorHandler.message,
      });
    }
    return () => {
      dispatch(CleanupErrors());
    };
  }, [
    dispatch,
    error.errorHandler.message,
    error.hasNonScheduledPollErrors,
    error.hasScheduledPollErrors,
    error.isLoading,
  ]);

  return (
    <div className="poll-activity">
      <div className="poll-menu">
        <Row justify="center" align="center">
          <Col
            xs={{ span: 25, offset: 2 }}
            sm={{ span: 12, offset: 2 }}
            md={{ span: 9, offset: 1 }}
            lg={{
              span: 6,
              offset: 2,
            }}
          >
            <Tabs defaultActiveKey="1" onChange={callback}>
              <TabPane
                tab={
                  <span>
                    <FormOutlined />
                    Create Poll
                  </span>
                }
                key="1"
              >
                <div className="poll-section-1">
                  <NonScheduledPoll />
                </div>
              </TabPane>
              <Space />
              <TabPane
                tab={
                  <span>
                    <CalendarFilled />
                    Schedule Poll
                  </span>
                }
                key="2"
              >
                <div className="poll-section-1">
                  <ScheduledPoll />
                </div>
              </TabPane>
            </Tabs>
          </Col>
        </Row>
      </div>
    </div>
  );
}
export default CreatePollDashboard;
