import { Box, Button, Card, CardActions, CardContent, Grid, Modal, Typography } from "@mui/material";
import { useEffect, useState } from "react";
import { gridSpacing } from "store/constant";
import { apiRequest } from "utils/api-request";
import FloatingActionButtonZoom from "../../ui-component/FloatingActionButtonZoom"; // Ensure you have this import
import ChatWindow from "./ChatWindow"; // 채팅창 컴포넌트
import PeopleList from "./PeopleList"; // 사람들 목록 컴포넌트
import RoomCreationForm from "./roomCreationForm";

const RoomList = () => {
  const [isLoading, setLoading] = useState(true);
  const [rooms, setRooms] = useState([]);
  const [openRoomModal, setOpenRoomModal] = useState(false); // 방 참여 모달
  const [openCreateModal, setOpenCreateModal] = useState(false); // 방 생성 모달
  const [selectedRoom, setSelectedRoom] = useState(null);

  // Continent code mapping
  const continentMap = {
    VICT: "빅토리아 아일랜드",
    ELNA: "엘나스",
    LUDI: "루디브리엄",
    LEAF: "리프레"
  };

  // Map continent codes to tab indices
  const continentIndices = {
    VICT: 0,
    ELNA: 1,
    LUDI: 2,
    LEAF: 3
  };

  // 방 참여 모달 열기 및 닫기 함수
  const handleOpenRoomModal = (room) => {
    setSelectedRoom(room);
    setOpenRoomModal(true);
  };

  const handleCloseRoomModal = () => {
    setOpenRoomModal(false);
    setSelectedRoom(null);
  };

  // 방 생성 모달 열기 및 닫기 함수
  const handleOpenCreateModal = () => {
    setOpenCreateModal(true);
  };

  const handleCloseCreateModal = () => {
    setOpenCreateModal(false);
  };

  useEffect(() => {
    const loadRooms = async () => {
      const url = import.meta.env.VITE_APP_API_URL;
      console.log("URL : ", url);
      const data = await apiRequest.fetchData(`${url}/api/room`);
      if (data.success) {
        setRooms(data.data);
        setLoading(false);
      }
    };

    loadRooms();
  }, []);

  // Prepare components for each tab (continent)
  const continentRooms = [[], [], [], []]; // Initialize arrays for each continent

  rooms.forEach((room) => {
    const index = continentIndices[room.continent];
    if (index !== undefined) {
      continentRooms[index].push(room);
    }
  });

  const components = continentRooms.map((roomsInContinent, idx) => (
    <Grid container spacing={2} key={idx}>
      {roomsInContinent.length > 0 ? (
        roomsInContinent.map((room) => (
          <Grid item xs={12} sm={6} md={4} key={room.id}>
            <Card sx={{ bgcolor: "background.paper", boxShadow: 3 }}>
              <CardContent>
                <Typography
                  variant="h5"
                  component="div"
                  sx={{
                    mb: 1.5,
                    color: "grey[900]",
                    fontWeight: "bold"
                  }}
                >
                  {room.name}
                </Typography>
                <Typography sx={{ mb: 1.5, color: "secondary.main" }} color="text.secondary">
                  파티장: {room.master_name}
                </Typography>
                <Typography variant="body2" sx={{ color: "text.secondary" }}>
                  현재 인원: {room.capacity}
                </Typography>
              </CardContent>
              <CardActions>
                <Button
                  size="small"
                  color="primary"
                  sx={{
                    color: "white",
                    bgcolor: "secondary.main",
                    "&:hover": { bgcolor: "secondary.dark" }
                  }}
                  onClick={() => handleOpenRoomModal(room)}
                >
                  참여하기
                </Button>
              </CardActions>
            </Card>
          </Grid>
        ))
      ) : (
        <Grid item xs={12}>
          <Typography variant="body1">등록된 방이 없습니다.</Typography>
        </Grid>
      )}
    </Grid>
  ));

  return (
    <Grid container spacing={gridSpacing}>
      {/* 방 생성 버튼 - 우측 상단에 위치 */}
      <Grid item xs={12} display="flex" justifyContent="flex-end" sx={{ mb: 2 }}>
        <Button variant="contained" color="secondary" onClick={handleOpenCreateModal}>
          파티 생성
        </Button>
      </Grid>

      {/* Pass components to FloatingActionButtonZoom */}
      <FloatingActionButtonZoom components={components} />

      {/* 방 참여 모달 */}
      {selectedRoom && (
        <Modal open={openRoomModal} onClose={handleCloseRoomModal}>
          <Box
            sx={{
              position: "absolute",
              top: "50%",
              left: "50%",
              transform: "translate(-50%, -50%)",
              width: 600,
              bgcolor: "background.paper",
              boxShadow: 24,
              p: 4,
              borderRadius: 2,
              maxHeight: "80vh",
              overflowY: "auto"
            }}
          >
            <Typography variant="h6" sx={{ mb: 2 }}>
              {selectedRoom.name}
            </Typography>
            <Grid container spacing={2}>
              <Grid item xs={12} md={4}>
                {/* 사람들 목록 */}
                <PeopleList roomId={selectedRoom.id} />
              </Grid>
              <Grid item xs={12} md={8}>
                {/* 채팅창 */}
                <ChatWindow roomId={selectedRoom.id} />
              </Grid>
            </Grid>
          </Box>
        </Modal>
      )}

      {/* 방 생성 모달 */}
      <Modal open={openCreateModal} onClose={handleCloseCreateModal}>
        <Box
          sx={{
            position: "absolute",
            top: "50%",
            left: "50%",
            transform: "translate(-50%, -50%)",
            width: 400,
            bgcolor: "background.paper",
            boxShadow: 24,
            p: 4,
            borderRadius: 2
          }}
        >
          <RoomCreationForm onClose={handleCloseCreateModal} />
        </Box>
      </Modal>
    </Grid>
  );
};

export default RoomList;
