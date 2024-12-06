import { apiRequest } from "utils/api-request";

// material-ui
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import Grid from "@mui/material/Grid";
import { useTheme } from "@mui/material/styles";
import useMediaQuery from "@mui/material/useMediaQuery";

// project imports
import AnimateButton from "ui-component/extended/AnimateButton";

import Kakao from "assets/images/icons/social-kakao.png";

const AuthLogin = () => {
  const theme = useTheme();
  const matchDownSM = useMediaQuery(theme.breakpoints.down("md"));
  // const customization = useSelector((state) => state.customization);
  // const [checked, setChecked] = useState(true);

  // const googleHandler = async () => {
  //   console.error('Login');
  // };

  // const [showPassword, setShowPassword] = useState(false);
  // const handleClickShowPassword = () => {
  //   setShowPassword(!showPassword);
  // };

  // const handleMouseDownPassword = (event) => {
  //   event.preventDefault();
  // };

  const buttonStyles = {
    color: "grey.700",
    backgroundColor: theme.palette.grey[50],
    borderColor: theme.palette.grey[100]
  };

  //TODO : 여기 매소드 컴포넌트 따로 뺴서 만들기
  const kakaoLogin = () => {
    const url = import.meta.env.VITE_APP_API_URL;
    apiRequest.pageRedirect(`${url}/oauth2/authorization/kakao`);
  };

  return (
    <>
      <Grid container direction="column" justifyContent="center" spacing={2}>
        {/* <Grid item xs={12}>
          <AnimateButton>
            <Button fullWidth variant="outlined" sx={buttonStyles} onClick={() => console.error('Login')}>
              <Box sx={{ mr: matchDownSM ? 1 : 2 }}>
                <img src={Google} alt="google" width={16} height={16} />
              </Box>
              구글로 로그인
            </Button>
          </AnimateButton>
        </Grid> */}
        <Grid item xs={12}>
          <AnimateButton>
            <Button fullWidth variant="outlined" sx={buttonStyles} onClick={() => kakaoLogin()}>
              <Box sx={{ mr: matchDownSM ? 1 : 2 }}>
                <img src={Kakao} alt="kakao" width={20} height={20} />
              </Box>
              카카오로 로그인
            </Button>
          </AnimateButton>
        </Grid>
      </Grid>
    </>
  );
};

export default AuthLogin;
