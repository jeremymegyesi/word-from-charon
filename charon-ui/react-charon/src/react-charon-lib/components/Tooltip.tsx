"use client";

import * as React from "react";
import { Tooltip } from "react-tooltip";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCircleInfo } from '@fortawesome/free-solid-svg-icons';

interface TooltipProps {
  children: React.ReactNode;
  tooltipKey: string;
  content?: string;
}

function CustomTooltip({
  children,
  tooltipKey,
  ...props
}: TooltipProps) {
  return (
    <a className={`${tooltipKey}-anchor text-primary`} key={tooltipKey}>
      {children}
      <FontAwesomeIcon className="text-primary" icon={faCircleInfo} /><Tooltip anchorSelect={`.${tooltipKey}-anchor`} place="bottom">{props.content}</Tooltip>
    </a>
  );
}

export { CustomTooltip as Tooltip };
