import React from 'react';

const BtnPanel: React.FC<{ left?: React.ReactNode; right?: React.ReactNode }> = ({
  left,
  right,
}) => {
  return (
    <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 16 }}>
      <div className="left">{left}</div>
      <div className="right">{right}</div>
    </div>
  );
};

export default BtnPanel;
