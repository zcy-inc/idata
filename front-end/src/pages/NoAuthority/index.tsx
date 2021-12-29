import React, { useEffect, useState } from 'react';
import { getSideMenu } from '@/services/global';
import { IMenuItem } from '@/types/common';

const NoAuthority = () => {
  const [menuList, setMenuList] = useState<IMenuItem[]>([]);

  useEffect(() => {
    getSideMenu()
      .then((res) => {
        setMenuList(res.data);
      })
      .catch((err) => {});
  }, []);

  return (
    <div
      style={{
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100%',
        backgroundColor: '#fff',
      }}
    >
      {!menuList.length && (
        <>
          <img
            src="https://sitecdn.zcycdn.com/f2e-assets/da28f74f-fefa-4bc5-827c-51bb26f86454.png"
            alt="img"
            style={{ width: 400 }}
          />
          <p
            style={{
              fontSize: 14,
              color: '#737B96',
              marginTop: 20,
            }}
          >
            目前暂无权限，请联系管理员开通
          </p>
        </>
      )}
    </div>
  );
};

export default NoAuthority;
