import * as React from "react";
import PropTypes from "prop-types";
import { useTheme } from "@mui/material/styles";
import AppBar from "@mui/material/AppBar";
import Tabs from "@mui/material/Tabs";
import Tab from "@mui/material/Tab";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";

function TabPanel(props) {
  const { children, value, index, ...other } = props;

  if (value === 0) {
    console.log("게임 탭이 선택되었습니다.");
  } else if (value === 1) {
    console.log("수다 탭이 선택되었습니다.");
  } else if (value === 2) {
    console.log("루디브리엄 탭이 선택되었습니다.");
  } else if (value === 3) {
    console.log("리프레 탭이 선택되었습니다.");
  }

  return (
    <Typography
      component="div"
      role="tabpanel"
      hidden={value !== index}
      id={`action-tabpanel-${index}`}
      aria-labelledby={`action-tab-${index}`}
      {...other}
    >
      {value === index && <Box sx={{ p: 3 }}>{children}</Box>}
    </Typography>
  );
}

TabPanel.propTypes = {
  children: PropTypes.node,
  index: PropTypes.number.isRequired,
  value: PropTypes.number.isRequired,
};

function a11yProps(index) {
  return {
    id: `action-tab-${index}`,
    "aria-controls": `action-tabpanel-${index}`,
  };
}

export default function FloatingActionButtonZoom({ components }) {
  const theme = useTheme();
  const [value, setValue] = React.useState(0);

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  return (
    <Box
      sx={{
        bgcolor: "background.paper",
        width: "100%",
        position: "relative",
        justifyContent: "center",
        alignItems: "center",
        minHeight: 200,
      }}
    >
      <AppBar position="static" color="default">
        <Tabs
          value={value}
          onChange={handleChange}
          indicatorColor=""
          textColor="secondary"
          variant="fullWidth"
          aria-label="action tabs example"
        >
          <Tab label="빅토리아 아일랜드" {...a11yProps(0)} />
          <Tab label="엘나스" {...a11yProps(1)} />
          <Tab label="루디브리엄" {...a11yProps(2)} />
          <Tab label="리프레" {...a11yProps(3)} />
        </Tabs>
      </AppBar>
      <TabPanel value={value} index={0} dir={theme.direction}>
        {components[0]}
      </TabPanel>
      <TabPanel value={value} index={1} dir={theme.direction}>
        {components[1]}
      </TabPanel>
      <TabPanel value={value} index={2} dir={theme.direction}>
        {components[2]}
      </TabPanel>
      <TabPanel value={value} index={3} dir={theme.direction}>
        {components[3]}
      </TabPanel>
    </Box>
  );
}

FloatingActionButtonZoom.propTypes = {
  components: PropTypes.arrayOf(PropTypes.node).isRequired,
};
