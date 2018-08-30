package utils

import (
	"fmt"
	"bytes"
	"crypto/x509"
	"encoding/pem"
	"strings"

	"github.com/hyperledger/fabric/core/chaincode/shim"
)

// 获取当前操作智能合约成员的具体名称，如a1aw28
func GetCreatorName(stub shim.ChaincodeStubInterface) (string, error) {
	name, err := GetCreator(stub) // 获取当前智能合约操作成员名称
	if err != nil {
		return "", err
	}
	// 格式化当前智能合约操作成员名称
	memberName := name[(strings.Index(name, "@") + 1):strings.LastIndex(name, ".example.com")]
	return memberName, nil
}

// 获取操作成员
func GetCreator(stub shim.ChaincodeStubInterface) (string, error) {
	creatorByte, _ := stub.GetCreator()
	certStart := bytes.IndexAny(creatorByte, "-----BEGIN")
	if certStart == -1 {
		fmt.Errorf("No certificate found")
	}
	certText := creatorByte[certStart:]
	bl, _ := pem.Decode(certText)
	if bl == nil {
		fmt.Errorf("Could not decode the PEM structure")
	}

	cert, err := x509.ParseCertificate(bl.Bytes)
	if err != nil {
		fmt.Errorf("ParseCertificate failed")
	}
	uname := cert.Subject.CommonName
	return uname, nil
}
