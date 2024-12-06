import { Link } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";

// material-ui
import ButtonBase from "@mui/material/ButtonBase";
import Typography from "@mui/material/Typography";

// project imports
import config from "config";
import Logo from "ui-component/Logo";
import { MENU_OPEN } from "store/actions";

// ==============================|| MAIN LOGO ||============================== //

const LogoSection = () => {
  const defaultId = useSelector((state) => state.customization.defaultId);
  const dispatch = useDispatch();
  return (
    <ButtonBase
      disableRipple
      onClick={() => dispatch({ type: MENU_OPEN, id: defaultId })}
      component={Link}
      to={config.defaultPath}
    >
      <Logo />
      <Typography variant="h5" component="span" sx={{ marginLeft: 6 }}>
        메랜인
      </Typography>
    </ButtonBase>
  );
};

export default LogoSection;
